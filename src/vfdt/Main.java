package vfdt;

import vfdt.data.*;
import vfdt.measure.bound.Bound;
import vfdt.measure.bound.BoundHoeffding;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.GainBase;
import vfdt.measure.impurity.GiniIndex;
import vfdt.measure.impurity.Impurity;
import vfdt.measure.impurity.InformationEntropy;
import vfdt.stat.SuffStatFactory;
import vfdt.stat.SuffStatFactoryBase;
import vfdt.stat.attstat.AttStatFactoryBase;
import vfdt.stat.splitter.SplitterFactoryBase;
import vfdt.tree.DecisionTree;
import vfdt.tree.SplitPolicy;
import vfdt.tree.VFDT;
import vfdt.util.Pair;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        String  fileName   = "D:/data/rbf-a30-c6-n1e5.arff";
        Integer classIndex = 30;

        try {
            // Analyze dataset
            DatasetReader reader = new ArffReader(fileName);
            DatasetInfo datasetInfo = reader.analyze();
            datasetInfo.setClassIndex(classIndex);

            // hyper parameters
            int gracePeriod = 200;
            Double delta = 0.01;
            Double R = 6.;
            Double tieBreak = 0.05;
            Bound bound = new BoundHoeffding(delta, R, tieBreak);
            int maxHeight = 5;
//            Impurity impurity = new GiniIndex();
            Impurity impurity = new InformationEntropy();
            Gain gain = new GainBase(impurity);
            int numCadidates = 10;

            SplitPolicy splitPolicy = new SplitPolicy(gracePeriod, bound, maxHeight);
            SuffStatFactory suffStatFactory = new SuffStatFactoryBase(
                    new AttStatFactoryBase(datasetInfo),
                    new SplitterFactoryBase(gain, numCadidates)
            );

            // Create model
            DecisionTree tree = new VFDT(datasetInfo, splitPolicy, suffStatFactory);

            // Train model
            DatasetIterator iter = reader.onePass();
            int counter = 0;
            while (iter.hasNext()) {
                if (counter % 2000 == 0)
                    System.out.println(counter);
                Pair<Instance, Attribute> entry = iter.next();
                Instance instance = entry.getFirst();
                Attribute label = entry.getSecond();
                tree.learn(instance, label);
                counter += 1;
            }
            iter.close();

            System.out.println(tree.print());

            // Test model
            double accuracy = 0;
            int numData = 0;
            iter = reader.onePass();
            while (iter.hasNext()) {
                Pair<Instance, Attribute> entry = iter.next();
                Instance instance = entry.getFirst();
                Attribute label = entry.getSecond();
                String pred = tree.classify(instance);
                if (pred.equals(label.getValue()))
                    accuracy += 1;
                numData += 1;
            }
            iter.close();
            accuracy /= numData;
            System.out.println("Accuracy = " + accuracy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
