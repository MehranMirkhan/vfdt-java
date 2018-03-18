package vfdt.ml;

import vfdt.data.ArffReader;
import vfdt.data.DatasetInfo;
import vfdt.data.DatasetReader;
import vfdt.util.Pair;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 17
 */
public abstract class TrainMethod {
    protected ClassifierFactory classifierFactory;
    protected DatasetInfo       datasetInfo;
    protected String            trainFile;
    protected int               numEpochs;

    public TrainMethod(ClassifierFactory classifierFactory, DatasetInfo datasetInfo, String trainFile, int numEpochs) {
        this.classifierFactory = classifierFactory;
        this.datasetInfo = datasetInfo;
        this.trainFile = trainFile;
        this.numEpochs = numEpochs;
    }

    public Classifier onlyTrain() throws Exception {
        DatasetReader reader = new ArffReader(trainFile);
        reader.setDatasetInfo(datasetInfo);
        return Evaluator.train(classifierFactory, reader, numEpochs);
    }

    public abstract Pair<Classifier, Double> evaluate() throws Exception;
}
