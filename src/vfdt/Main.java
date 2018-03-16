package vfdt;

import vfdt.data.*;
import vfdt.measure.bound.Bound;
import vfdt.measure.bound.BoundGini;
import vfdt.measure.bound.BoundHoeffding;
import vfdt.measure.bound.BoundMisclassification;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.GainBase;
import vfdt.measure.impurity.GiniIndex;
import vfdt.measure.impurity.Impurity;
import vfdt.measure.impurity.InformationEntropy;
import vfdt.measure.impurity.MisclassificationError;
import vfdt.ml.Evaluator;
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
            datasetInfo.setNumData(100000);

            // hyper parameters
            int gracePeriod = 1000;
            Double delta = 1e-6;
            int numClasses = datasetInfo.getNumClasses();
            Double R = Math.log(numClasses) / Math.log(2);
            Double tieBreak = 0.025;
//            Bound bound = new BoundHoeffding(delta, R, tieBreak);
//            Bound bound = new BoundGini(delta, tieBreak, numClasses);
            Bound bound = new BoundMisclassification(delta, tieBreak);
            int maxHeight = 8;
//            Impurity impurity = new GiniIndex();
//            Impurity impurity = new InformationEntropy();
            Impurity impurity = new MisclassificationError();
            double minBranchFrac = 1e-2;
            Gain gain = new GainBase(impurity, minBranchFrac);
            int numCadidates = 10;
            String[] splitters = {"bin", "exact", "delayed"};

            SplitPolicy splitPolicy = new SplitPolicy(gracePeriod, bound, maxHeight);
            SuffStatFactory suffStatFactory = new SuffStatFactoryBase(
                    new AttStatFactoryBase(datasetInfo),
                    new SplitterFactoryBase(gain, numCadidates, splitters[0])
            );

            // Create model
            DecisionTree tree = new VFDT(datasetInfo, splitPolicy, suffStatFactory);

            long startTime = System.currentTimeMillis();
            System.out.println("Accuracy = " + Evaluator.percentile(tree, reader, 0.66));
            System.out.println(tree.print());
            long endTime = System.currentTimeMillis();
            System.out.println("--- " + (endTime - startTime)/1e3 + " seconds ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
