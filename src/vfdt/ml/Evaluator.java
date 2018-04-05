package vfdt.ml;

import vfdt.data.*;
import vfdt.util.Pair;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class Evaluator {
    public static Classifier train(ClassifierFactory factory, DatasetReader reader, int numEpochs, StopCriterion stopCriterion) throws Exception {
        Classifier model = factory.build();
        // Train model
        DatasetIterator iter = reader.epochs(numEpochs);
        while (iter.hasNext()) {
            Pair<Instance, Attribute> entry = iter.next();
            Instance instance = entry.getFirst();
            Attribute label = entry.getSecond();
            model.learn(instance, label);
        }
        iter.close();
        return model;
    }

    public static Pair<Classifier, Double> evaluate(ClassifierFactory factory,
                                                    DatasetReader reader,
                                                    int numEpochs,
                                                    IndexCondition trainCondition,
                                                    IndexCondition testCondition,
                                                    StopCriterion stopCriterion) throws Exception {
        Classifier model = factory.build();

        // Train model
        DatasetIterator iter = reader.epochs(numEpochs, trainCondition);
        while (iter.hasNext()) {
            Pair<Instance, Attribute> entry = iter.next();
            Instance instance = entry.getFirst();
            Attribute label = entry.getSecond();
            model.learn(instance, label);
            if (stopCriterion != null && stopCriterion.shouldStop(model)) break;
        }
        iter.close();

        // Test model
        iter = reader.onePass(testCondition);
        Double accuracy    = 0.0;
        int    numTestData = 0;
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
        return new Pair<>(model, accuracy);
    }

    public static Pair<Classifier, Double> evaluateSeparate(ClassifierFactory factory,
                                                            DatasetReader trainReader,
                                                            DatasetReader testReader,
                                                            int numEpochs,
                                                            StopCriterion stopCriterion) throws Exception {
        Classifier model = factory.build();

        // Train model
        DatasetIterator iter = trainReader.epochs(numEpochs);
        while (iter.hasNext()) {
            Pair<Instance, Attribute> entry = iter.next();
            Instance instance = entry.getFirst();
            Attribute label = entry.getSecond();
            model.learn(instance, label);
            if (stopCriterion != null && stopCriterion.shouldStop(model)) break;
        }
        iter.close();

        // Test model
        iter = testReader.onePass();
        Double accuracy    = 0.0;
        int    numTestData = 0;
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
        return new Pair<>(model, accuracy);
    }
}
