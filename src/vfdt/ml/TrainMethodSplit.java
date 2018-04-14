package vfdt.ml;

import json.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfdt.data.*;
import vfdt.tree.DecisionTree;
import vfdt.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 17
 */
public class TrainMethodSplit extends TrainMethod {
    private Double percent;
    private static final Logger logger = LogManager.getLogger();

    public TrainMethodSplit(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                            JSONArray trainFile, Double percent, int numEpochs, StopCriterion stopCriterion) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs, stopCriterion);
        this.percent = percent;
    }

    @Override
    public List<Pair<Classifier, Double>> evaluate() throws Exception {
        logger.traceEntry();
        List<Pair<Classifier, Double>> results = new ArrayList<>();

        for (int i = 0; i < trainFile.length(); i++) {
            String fileName = (String) trainFile.get(i);
            logger.info("Experiment on file \"" + fileName + "\"");
            DatasetReader reader = new ArffReader(fileName);
            reader.setDatasetInfo(datasetInfo);
            Integer numData = datasetInfo.getNumData();
            Integer        numTrain       = (int) (numData * percent);
            IndexCondition trainCondition = new IndexConditionBetween(0, numTrain);
            IndexCondition testCondition  = new IndexConditionBetween(numTrain, numData);
            Pair<Classifier, Double> result = Evaluator.evaluate(
                    classifierFactory, reader, numEpochs, trainCondition, testCondition, stopCriterion);
            results.add(result);
        }
        Double accuracy = 0., height = 0., numNodes = 0., numLeaves = 0.;
        for (Pair<Classifier, Double> result : results) {
            accuracy += result.getSecond();
            DecisionTree dt = (DecisionTree) result.getFirst();
            height += dt.getHeight();
            numNodes += dt.getNumNodes();
            numLeaves += dt.getNumLeaves();
        }

        int numExperiments = results.size();
        accuracy /= numExperiments;
        height /= numExperiments;
        numNodes /= numExperiments;
        numLeaves /= numExperiments;

        logger.info("Average accuracy = " + accuracy);
        logger.info("Average size     = " + numNodes);
        logger.info("Average leaves   = " + numLeaves);
        logger.info("Average height   = " + height);

        return logger.traceExit(results);
    }
}
