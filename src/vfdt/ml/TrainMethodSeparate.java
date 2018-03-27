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
public class TrainMethodSeparate extends TrainMethod {

    private final String testFile;

    public TrainMethodSeparate(ClassifierFactory classifierFactory, DatasetInfo datasetInfo,
                               String trainFile, String testFile, int numEpochs) {
        super(classifierFactory, datasetInfo, trainFile, numEpochs);
        this.testFile = testFile;
    }

    @Override
    public Pair<Classifier, Double> evaluate() throws Exception {
        DatasetReader trainReader = new ArffReader(trainFile);
        trainReader.setDatasetInfo(datasetInfo);
        Integer        numDataTrain   = datasetInfo.getNumData();
        IndexCondition conditionTrain = new IndexConditionBetween(0, numDataTrain);
        DatasetReader  testReader     = new ArffReader(testFile);

        return Evaluator.evaluateSeparate(classifierFactory, trainReader, testReader, numEpochs, conditionTrain);
    }
}
