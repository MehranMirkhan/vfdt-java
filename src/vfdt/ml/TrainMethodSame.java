package vfdt.ml;

import vfdt.data.*;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 17
 */
public class TrainMethodSame extends TrainMethod {

    public TrainMethodSame(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                           String trainFile, int numEpochs) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs);
    }

    @Override
    public Double evaluate() throws Exception {
        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        Integer        numData   = datasetInfo.getNumData();
        IndexCondition condition = new IndexConditionBetween(0, numData);

        return Evaluator.evaluate(classifierFactory, reader, numEpochs, condition, condition);
    }
}
