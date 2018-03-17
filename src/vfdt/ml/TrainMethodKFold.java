package vfdt.ml;

import vfdt.data.*;

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
    public Double evaluate() throws Exception {
        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        Integer       numData     = datasetInfo.getNumData();
        Integer       step        = numData / k;
        DecimalFormat df          = new DecimalFormat("0.00000");
        Double        accuracy    = 0.;
        for (int i = 0; i < k; i++) {
            System.out.print("Fold " + i + ": ");
            IndexCondition trainCondition = new IndexConditionNotBetween(i * step, (i + 1) * step);
            IndexCondition testCondition = new IndexConditionBetween(i * step, (i + 1) * step);
            Double acc = Evaluator.evaluate(classifierFactory, reader, numEpochs, trainCondition, testCondition);
            accuracy += acc;
            System.out.println("Accuracy = " + df.format(acc));
        }

        return accuracy / k;
    }
}
