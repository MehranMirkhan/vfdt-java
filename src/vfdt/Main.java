package vfdt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfdt.ml.Classifier;
import vfdt.ml.TrainMethod;
import vfdt.tree.TreeMaker;
import vfdt.util.Config;
import vfdt.util.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {
        train();
//        synthesize();
    }

    static void train() throws Exception {
        Logger logger = LogManager.getLogger();
        logger.traceEntry();
        String[] rbf_exp = {
                "rbf-a5-c50-k10-n1e5",       // 0
                "rbf-a10-c20-k2-n1e5",       // 1
                "rbf-a10-c20-k3-n1e5",       // 2
                "rbf-a10-c20-k10-n1e5",      // 3
                "rbf-a10-c50-k2-n1e5",       // 4
                "rbf-a10-c50-k3-n1e5",       // 5
                "rbf-a10-c50-k4-n1e5",       // 6
                "rbf-a10-c50-k6-n1e5",       // 7
                "rbf-a10-c50-k8-n1e5",       // 8
                "rbf-a10-c50-k10-n1e5",      // 9
                "rbf-a15-c50-k10-n1e5",      // 10
                "rbf-a20-c50-k10-n1e5",      // 11
        };
        for (int i = 0; i < rbf_exp.length; i++) rbf_exp[i] = "rbf/" + rbf_exp[i];
        String[] dt_exp = {
                "dt-a10-k10-v(0,1)-h(3,10)-p(0.25)-n1e4",   // 0
        };
        for (int i = 0; i < dt_exp.length; i++) dt_exp[i] = "dt/" + dt_exp[i];
        String paramFileName = "params/" + dt_exp[0] + ".json";
        paramFileName = paramFileName.replaceFirst("^~", System.getProperty("user.home"));
        logger.info("Parameter file: " + paramFileName);
        Config config = new Config(paramFileName);
        TrainMethod trainMethod = (TrainMethod) config.getParam("trainMethod");

        long startTime = System.currentTimeMillis();
        List<Pair<Classifier, Double>> results = trainMethod.evaluate();
        long endTime = System.currentTimeMillis();
        logger.info("Training time: " + (endTime - startTime) / 1e3 + " seconds");
        logger.traceExit();}

    static void synthesize() throws IOException {
        Integer numAttributes = 10;
        Integer numClasses = 10;
        Double minAttValue = 0.;
        Double maxAttValue = 1.;
        Integer minHeight = 3;
        Integer maxHeight = 10;
        Double leafProb = 0.25;
        long numInstances = (long) 1e4;
        String datasetName = String.format("dt-a%d-k%d-v(%.2f,%.2f)-h(%d,%d)-p(%.2f)-n%.0e",
                numAttributes, numClasses,
                minAttValue, maxAttValue,
                minHeight, maxHeight,
                leafProb, (double) numInstances);
        long seed = 0;
        Random rand = new Random(seed);
        TreeMaker.SimpleDecisionTree tree = TreeMaker.createTree(rand,
                numAttributes, numClasses, minAttValue, maxAttValue,
                minHeight, maxHeight, leafProb, seed);
//        System.out.println(tree.getDatasetInfo());
//        System.out.println(tree.draw());
//        System.out.println(datasetName);
        tree.getDatasetInfo().setDatasetName(datasetName);
        String filePath = "/home/mehran/data/dt/" + datasetName + ".arff";
        TreeMaker.generateDataset(rand, tree, minAttValue, maxAttValue,
                filePath, numInstances);
    }
}
