package vfdt.ml;

import vfdt.data.*;
import vfdt.tree.DecisionTree;
import vfdt.tree.VFDT;
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
        Double     accuracy = 0., height = 0., numNodes = 0., numLeaves = 0.;
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

        System.out.println("Average numNodes  = " + numNodes);
        System.out.println("Average numLeaves = " + numLeaves);
        System.out.println("Average height    = " + height);

        return new Pair<>(model, accuracy);
    }

    private class FoldTrainer extends Thread {
        private int                      fold;
        private ClassifierFactory        classifierFactory;
        private DatasetReader            reader;
        private int                      numEpochs;
        private int                      step;
        private Pair<Classifier, Double> result;

        public FoldTrainer(int fold, ClassifierFactory classifierFactory,
                           DatasetReader reader, int numEpochs, int step) {
            super("Fold_" + fold);
            this.fold = fold;
            this.classifierFactory = classifierFactory;
            this.reader = reader;
            this.numEpochs = numEpochs;
            this.step = step;
            result = null;
        }

        @Override
        public void run() {
            super.run();
//            DecimalFormat  df             = new DecimalFormat("0.00000");
            IndexCondition trainCondition = new IndexConditionNotBetween(fold * step, (fold + 1) * step);
            IndexCondition testCondition  = new IndexConditionBetween(fold * step, (fold + 1) * step);
            try {
                result = Evaluator.evaluate(classifierFactory, reader,
                        numEpochs, trainCondition, testCondition);
                System.out.println("Fold " + fold + ": Accuracy = " + result.getSecond());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Pair<Classifier, Double> getResult() {
            return result;
        }
    }
}
