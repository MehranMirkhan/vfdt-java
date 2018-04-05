package vfdt.ml;

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
                            String trainFile, Double percent, int numEpochs, StopCriterion stopCriterion) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs, stopCriterion);
        this.percent = percent;
    }

    @Override
    public List<Pair<Classifier, Double>> evaluate() throws Exception {
        logger.traceEntry();
        List<Pair<Classifier, Double>> results = new ArrayList<>();

        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        Integer        numData        = datasetInfo.getNumData();
        Integer        numTrain       = (int) (numData * percent);
        IndexCondition trainCondition = new IndexConditionBetween(0, numTrain);
        IndexCondition testCondition  = new IndexConditionBetween(numTrain, numData);
        Pair<Classifier, Double> result = Evaluator.evaluate(
                classifierFactory, reader, numEpochs, trainCondition, testCondition, stopCriterion);
        DecisionTree tree = (DecisionTree) result.getFirst();
        logger.info("Accuracy = " + result.getSecond());
        logger.info("Size     = " + tree.getNumNodes());
        logger.info("Leaves   = " + tree.getNumLeaves());
        logger.info("Height   = " + tree.getHeight());
        results.add(result);
        return logger.traceExit(results);
    }
}
