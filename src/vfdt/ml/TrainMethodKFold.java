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
public class TrainMethodKFold extends TrainMethod {
    private final int k;
    private static final Logger logger = LogManager.getLogger();

    public TrainMethodKFold(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                            JSONArray trainFile, int k, int numEpochs, StopCriterion stopCriterion) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs, stopCriterion);
        this.k = k;
    }

    @Override
    public List<Pair<Classifier, Double>> evaluate() throws Exception {
        logger.traceEntry();
        List<Pair<Classifier, Double>> results = new ArrayList<>();
        List<Double> exeTimes = new ArrayList<>();

        for (int i = 0; i < trainFile.length(); i++) {
            String fileName = (String) trainFile.get(i);
            logger.info("Experiment on file \"" + fileName + "\"");
            DatasetReader reader = new ArffReader(fileName);
            reader.setDatasetInfo(datasetInfo);
            Integer numData = datasetInfo.getNumData();
            Integer step = numData / k;
            FoldTrainer[] trainers = new FoldTrainer[k];
            for (int j = 0; j < k; j++)
                trainers[j] = new FoldTrainer(j, reader, step);
            for (FoldTrainer trainer : trainers)
                trainer.start();
            for (FoldTrainer trainer : trainers)
                trainer.join();
            for (FoldTrainer trainer : trainers) {
                results.add(trainer.getResult());
                exeTimes.add(trainer.getExeTime());
            }
        }
        Double accuracy = 0., height = 0., numNodes = 0., numLeaves = 0.;
        for (Pair<Classifier, Double> result : results) {
            accuracy += result.getSecond();
            DecisionTree dt = (DecisionTree) result.getFirst();
            height += dt.getHeight();
            numNodes += dt.getNumNodes();
            numLeaves += dt.getNumLeaves();
        }
        Double avgExeTime = 0.;
        for (Double exeTime : exeTimes)
            avgExeTime += exeTime;

        int numExperiments = results.size();
        accuracy /= numExperiments;
        height /= numExperiments;
        numNodes /= numExperiments;
        numLeaves /= numExperiments;
        avgExeTime /= numExperiments;

        logger.info("Average accuracy = " + accuracy);
        logger.info("Average size     = " + numNodes);
        logger.info("Average leaves   = " + numLeaves);
        logger.info("Average height   = " + height);
        logger.info("Average time     = " + avgExeTime);

        return logger.traceExit(results);
    }

    private class FoldTrainer extends Thread {
        private int fold;
        private DatasetReader reader;
        private int step;
        private Pair<Classifier, Double> result;
        private double exeTime;
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
                long startTime = System.currentTimeMillis();
                result = Evaluator.evaluate(classifierFactory, reader,
                        numEpochs, trainCondition, testCondition, stopCriterion);
//                logger.info("Fold " + fold + ": Accuracy = " + result.getSecond());
                long endTime = System.currentTimeMillis();
                exeTime = (endTime - startTime) / 1e3;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                logger.traceExit();
            }
        }

        public Pair<Classifier, Double> getResult() {
            return result;
        }

        public double getExeTime() {
            return exeTime;
        }
    }
}
