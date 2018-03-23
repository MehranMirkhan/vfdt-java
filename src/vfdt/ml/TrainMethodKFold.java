package vfdt.ml;

import vfdt.data.*;
import vfdt.util.Pair;

import java.text.DecimalFormat;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 17
 */
public class TrainMethodKFold extends TrainMethod {
    private final int k;

    public TrainMethodKFold(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                            String trainFile, int numEpochs, int k) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs);
        this.k = k;
    }

    @Override
    public Pair<Classifier, Double> evaluate() throws Exception {
        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        Integer       numData  = datasetInfo.getNumData();
        Integer       step     = numData / k;
        FoldTrainer[] trainers = new FoldTrainer[k];
        for (int i = 0; i < k; i++)
            trainers[i] = new FoldTrainer(i, classifierFactory, reader, numEpochs, step);
        for (FoldTrainer trainer : trainers)
            trainer.start();
        for (FoldTrainer trainer : trainers)
            trainer.join();
        Classifier model    = trainers[0].getResult().getFirst();
        Double     accuracy = 0.;
        for (FoldTrainer trainer : trainers) {
            accuracy += trainer.getResult().getSecond();
        }

        return new Pair<>(model, accuracy / k);
    }

    private class FoldTrainer extends Thread {
        private int                      fold;
        private ClassifierFactory        classifierFactory;
        private DatasetReader            reader;
        private int                      numEpochs;
        private int                      step;
        private Pair<Classifier, Double> result;

        public FoldTrainer(int fold, ClassifierFactory classifierFactory, DatasetReader reader, int numEpochs, int step) {
            this.fold = fold;
            this.classifierFactory = classifierFactory;
            this.reader = reader;
            this.numEpochs = numEpochs;
            this.step = step;
        }

        @Override
        public void run() {
            super.run();
            DecimalFormat  df             = new DecimalFormat("0.00000");
            IndexCondition trainCondition = new IndexConditionNotBetween(fold * step, (fold + 1) * step);
            IndexCondition testCondition  = new IndexConditionBetween(fold * step, (fold + 1) * step);
            try {
                result = Evaluator.evaluate(classifierFactory, reader,
                        numEpochs, trainCondition, testCondition);
                System.out.println("Fold " + fold + ": Accuracy = " + df.format(result.getSecond()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Pair<Classifier, Double> getResult() {
            return result;
        }
    }
}
