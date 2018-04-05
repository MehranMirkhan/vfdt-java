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
public class TrainMethodKFold extends TrainMethod {
    private final int k;
    private static final Logger logger = LogManager.getLogger();

    public TrainMethodKFold(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                            String trainFile, int numEpochs, int k, StopCriterion stopCriterion) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs, stopCriterion);
        this.k = k;
    }

    @Override
    public List<Pair<Classifier, Double>> evaluate() throws Exception {
        logger.traceEntry();
        List<Pair<Classifier, Double>> result = new ArrayList<>();

        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        Integer numData = datasetInfo.getNumData();
        Integer step = numData / k;
        FoldTrainer[] trainers = new FoldTrainer[k];
        for (int i = 0; i < k; i++)
            trainers[i] = new FoldTrainer(i, reader, step);
        for (FoldTrainer trainer : trainers)
            trainer.start();
        for (FoldTrainer trainer : trainers)
            trainer.join();
        for (FoldTrainer trainer : trainers)
            result.add(trainer.getResult());
        Double accuracy = 0., height = 0., numNodes = 0., numLeaves = 0.;
        for (FoldTrainer trainer : trainers) {
            accuracy += trainer.getResult().getSecond();
            height += ((DecisionTree) trainer.getResult().getFirst()).getHeight();
            numNodes += ((DecisionTree) trainer.getResult().getFirst()).getNumNodes();
            numLeaves += ((DecisionTree) trainer.getResult().getFirst()).getNumLeaves();
        }

        accuracy /= k;
        height /= k;
        numNodes /= k;
        numLeaves /= k;

        logger.info("Average accuracy = " + accuracy);
        logger.info("Average size     = " + numNodes);
        logger.info("Average leaves   = " + numLeaves);
        logger.info("Average height   = " + height);

        return logger.traceExit(result);
    }

    private class FoldTrainer extends Thread {
        private int fold;
        private DatasetReader reader;
        private int step;
        private Pair<Classifier, Double> result;
        private final Logger logger = LogManager.getLogger();

        FoldTrainer(int fold, DatasetReader reader, int step) {
            super("Fold_" + fold);
            this.fold = fold;
            this.reader = reader;
            this.step = step;
            result = null;
        }

        @Override
        public void run() {
            super.run();
            logger.traceEntry();
            IndexCondition trainCondition = new IndexConditionNotBetween(fold * step, (fold + 1) * step);
            IndexCondition testCondition = new IndexConditionBetween(fold * step, (fold + 1) * step);
            try {
                result = Evaluator.evaluate(classifierFactory, reader,
                        numEpochs, trainCondition, testCondition, stopCriterion);
                logger.info("Fold " + fold + ": Accuracy = " + result.getSecond());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                logger.traceExit();
            }
        }

        public Pair<Classifier, Double> getResult() {
            return result;
        }
    }
}
