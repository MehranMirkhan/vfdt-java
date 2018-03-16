package vfdt.ml;

import vfdt.data.*;
import vfdt.util.Pair;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class Evaluator {
    public static Classifier train(ClassifierFactory factory, DatasetReader reader) throws Exception {
        Classifier model = factory.build();
        // Train model
        DatasetIterator iter = reader.onePass();
        while (iter.hasNext()) {
            Pair<Instance, Attribute> entry = iter.next();
            Instance instance = entry.getFirst();
            Attribute label = entry.getSecond();
            model.learn(instance, label);
        }
        iter.close();
        return model;
    }

    public static Double evaluate(ClassifierFactory factory, DatasetReader reader,
                                  IndexCondition trainCondition, IndexCondition testCondition) throws Exception {
        Classifier model = factory.build();

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

    public static Double percentile(ClassifierFactory factory, DatasetReader reader, Double percentile) throws Exception {
        DatasetInfo datasetInfo = reader.getDatasetInfo();
        Integer numData = datasetInfo.getNumData();
        Integer numTrain = (int)(numData * percentile);
        IndexCondition trainCondition = new IndexConditionBetween(0, numTrain);
        IndexCondition testCondition = new IndexConditionBetween(numTrain, numData);

        return evaluate(factory, reader, trainCondition, testCondition);
    }

    public static Double kfold(ClassifierFactory factory, DatasetReader reader, int k) throws Exception {
        DatasetInfo datasetInfo = reader.getDatasetInfo();
        Integer numData = datasetInfo.getNumData();
        Integer step = (int) (numData / k);
        DecimalFormat df = new DecimalFormat("0.00000");
        Double accuracy = 0.;
        for (int i=0; i<k; i++) {
            System.out.print("Fold " + i + ": ");
            IndexCondition trainCondition = new IndexConditionNotBetween(i * step, (i+1) * step);
            IndexCondition testCondition = new IndexConditionBetween(i * step, (i+1) * step);
            Double acc = evaluate(factory, reader, trainCondition, testCondition);;
            accuracy += acc;
            System.out.println("Accuracy = " + df.format(acc));
        }

        return accuracy / k;
    }
}
