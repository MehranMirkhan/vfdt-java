package vfdt;

import json.JSONObject;
import json.JSONTokener;
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
import vfdt.ml.Classifier;
import vfdt.ml.ClassifierFactory;
import vfdt.ml.TrainMethod;
import vfdt.stat.SuffStatFactory;
import vfdt.stat.SuffStatFactoryBase;
import vfdt.stat.attstat.AttStatFactoryBase;
import vfdt.stat.splitter.SplitterFactoryBase;
import vfdt.tree.DecisionTree;
import vfdt.tree.SplitPolicy;
import vfdt.tree.VFDT;
import vfdt.util.Config;
import vfdt.util.Logger;
import vfdt.util.Pair;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    public static Pair<DatasetReader, ClassifierFactory> loadParams(String paramFileName) throws Exception {
        InputStream stream     = new FileInputStream(paramFileName);
        JSONObject  obj        = new JSONObject(new JSONTokener(stream));
        String      fileName   = obj.getString("filename");
        Integer     classIndex = obj.getInt("classIndex");

        // Analyze dataset
        DatasetReader reader      = new ArffReader(fileName);
        DatasetInfo   datasetInfo = reader.analyze();
        datasetInfo.setClassIndex(classIndex);
        datasetInfo.setNumData(obj.getInt("numData"));

        // hyper parameters
        int    gracePeriod = obj.getInt("gracePeriod");
        Double delta       = obj.getDouble("delta");
        int    numClasses  = datasetInfo.getNumClasses();
        Double R, tieBreak;
        Bound  bound       = null;
        switch (obj.getString("bound").toLowerCase()) {
            case "hoeffding":
                R = Math.log(numClasses) / Math.log(2);
                tieBreak = obj.getDouble("tieBreak");
                bound = new BoundHoeffding(delta, R, tieBreak);
                break;
            case "gini":
                tieBreak = obj.getDouble("tieBreak");
                bound = new BoundGini(delta, tieBreak, numClasses);
                break;
            case "misclassification":
                tieBreak = obj.getDouble("tieBreak");
                bound = new BoundMisclassification(delta, tieBreak);
                break;
        }
        int      maxHeight = obj.getInt("maxHeight");
        Impurity impurity  = null;
        switch (obj.getString("impurity").toLowerCase()) {
            case "entropy":
                impurity = new InformationEntropy();
                break;
            case "gini":
                impurity = new GiniIndex();
                break;
            case "misclassification":
                impurity = new MisclassificationError();
                break;
        }
        double minBranchFrac = obj.getDouble("minBranchFrac");
        Gain   gain          = new GainBase(impurity, minBranchFrac);
        int    numBins       = obj.getInt("numBins");
        String splitter      = obj.getString("splitter");

        SplitPolicy splitPolicy = new SplitPolicy(gracePeriod, bound, maxHeight);
        SuffStatFactory suffStatFactory = new SuffStatFactoryBase(
                new AttStatFactoryBase(datasetInfo),
                new SplitterFactoryBase(gain, numBins, splitter)
        );

        // Create model
        ClassifierFactory classifierFactory = new ClassifierFactory() {
            @Override
            public Classifier build() throws Exception {
                return new VFDT(datasetInfo, splitPolicy, suffStatFactory);
            }
        };
        stream.close();
        return new Pair<>(reader, classifierFactory);
    }

    public static void main(String[] args) throws Exception {
        String[] exp = {
                "rbf-a5-c50-k10-n1e5",       // 0
                "rbf-a10-c20-k2-n1e5",       // 1
                "rbf-a10-c20-k10-n1e5",      // 2
                "rbf-a10-c50-k2-n1e5",       // 3
                "rbf-a10-c50-k4-n1e5",       // 4
                "rbf-a10-c50-k6-n1e5",       // 5
                "rbf-a10-c50-k8-n1e5",       // 6
                "rbf-a10-c50-k10-n1e5",      // 7
                "rbf-a15-c50-k10-n1e5",      // 8
                "rbf-a20-c50-k10-n1e5",      // 9
                "covertype"                  // 10
        };
        String      paramFileName = "params/" + exp[0] + ".json";
        Config      config        = new Config(paramFileName);
        TrainMethod trainMethod   = (TrainMethod) config.getParam("trainMethod");

        Logger.setDebug(false);

        long startTime = System.currentTimeMillis();
        if (Logger.isDebug()) {
            DecisionTree model = (DecisionTree) trainMethod.onlyTrain();
            System.out.println(model.print(true));
            System.out.println(Logger.getLog());
        } else {
            Pair<Classifier, Double> result = trainMethod.evaluate();
            System.out.println("Accuracy = " + result.getSecond());
            DecisionTree model = (DecisionTree) result.getFirst();
            System.out.println(model.print(false));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("--- " + (endTime - startTime) / 1e3 + " seconds ---");
    }
}
