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
public class TrainMethodSeparate extends TrainMethod {
    private final String testFile;
    private static final Logger logger = LogManager.getLogger();

    public TrainMethodSeparate(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                               JSONArray trainFile, String testFile, int numEpochs, StopCriterion stopCriterion) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs, stopCriterion);
        this.testFile = testFile;
    }

    @Override
    public List<Pair<Classifier, Double>> evaluate() throws Exception {
        logger.traceEntry();
        List<Pair<Classifier, Double>> results = new ArrayList<>();

        DatasetReader trainReader = new ArffReader((String) trainFile.get(0));
        trainReader.setDatasetInfo(datasetInfo);
        Integer        numDataTrain   = datasetInfo.getNumData();
        IndexCondition conditionTrain = new IndexConditionBetween(0, numDataTrain);
        DatasetReader  testReader     = new ArffReader(testFile);

        Pair<Classifier, Double> result = Evaluator.evaluateSeparate(
                classifierFactory, trainReader, testReader, numEpochs, stopCriterion);
        DecisionTree tree = (DecisionTree) result.getFirst();
        logger.info("Accuracy = " + result.getSecond());
        logger.info("Size     = " + tree.getNumNodes());
        logger.info("Leaves   = " + tree.getNumLeaves());
        logger.info("Height   = " + tree.getHeight());
        results.add(result);
        return logger.traceExit(results);
    }
}
