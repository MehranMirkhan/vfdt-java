package vfdt.ml;

import vfdt.data.*;
import vfdt.util.Pair;

import java.io.IOException;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class Evaluator {
    public static Double evaluate(Classifier model, DatasetReader reader,
                                  IndexCondition trainCondition, IndexCondition testCondition) throws Exception {
        // Train model
        DatasetIterator iter = reader.onePass(trainCondition);
        while (iter.hasNext()) {
            Pair<Instance, Attribute> entry = iter.next();
            Instance instance = entry.getFirst();
            Attribute label = entry.getSecond();
            model.learn(instance, label);
        }
        iter.close();

        // Test model
        iter = reader.onePass(testCondition);
        Double accuracy = 0.0;
        int numTestData = 0;
        while (iter.hasNext()) {
            Pair<Instance, Attribute> entry = iter.next();
            Instance instance = entry.getFirst();
            Attribute label = entry.getSecond();
            String pred = model.classify(instance);
            if (pred.equals(label.getValue()))
                accuracy += 1;
            numTestData += 1;
        }
        iter.close();
        accuracy /= numTestData;
        return accuracy;
    }

    public static Double percentile(Classifier model, DatasetReader reader, Double percentile) throws Exception {
        DatasetInfo datasetInfo = reader.getDatasetInfo();
        Integer numData = datasetInfo.getNumData();
        Integer numTrain = (int)(numData * percentile);
        IndexCondition trainCondition = new IndexConditionBetween(0, numTrain);
        IndexCondition testCondition = new IndexConditionBetween(numTrain, numData);

        return evaluate(model, reader, trainCondition, testCondition);
    }
}
