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
public class TrainMethodSame extends TrainMethod {
    private static final Logger logger = LogManager.getLogger();

    public TrainMethodSame(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                           String trainFile, int numEpochs, StopCriterion stopCriterion) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs, stopCriterion);
    }

    @Override
    public List<Pair<Classifier, Double>> evaluate() throws Exception {
        logger.traceEntry();
        List<Pair<Classifier, Double>> results = new ArrayList<>();

        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        Integer        numData   = datasetInfo.getNumData();
        IndexCondition condition = new IndexConditionBetween(0, numData);

        Pair<Classifier, Double> result = Evaluator.evaluate(
                classifierFactory, reader, numEpochs, condition, condition, stopCriterion);
        DecisionTree tree = (DecisionTree) result.getFirst();
        logger.info("Accuracy = " + result.getSecond());
        logger.info("Size     = " + tree.getNumNodes());
        logger.info("Leaves   = " + tree.getNumLeaves());
        logger.info("Height   = " + tree.getHeight());
        results.add(result);
        return logger.traceExit(results);
    }
}
