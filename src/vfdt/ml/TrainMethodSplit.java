package vfdt.ml;

import vfdt.data.*;
import vfdt.util.Pair;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 17
 */
public class TrainMethodSplit extends TrainMethod {
    private Double percent;

    public TrainMethodSplit(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                            String trainFile, int numEpochs, Double percent) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs);
        this.percent = percent;
    }

    @Override
    public Pair<Classifier, Double> evaluate() throws Exception {
        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        Integer        numData        = datasetInfo.getNumData();
        Integer        numTrain       = (int) (numData * percent);
        IndexCondition trainCondition = new IndexConditionBetween(0, numTrain);
        IndexCondition testCondition  = new IndexConditionBetween(numTrain, numData);

        return Evaluator.evaluate(classifierFactory, reader, numEpochs, trainCondition, testCondition);
    }
}
