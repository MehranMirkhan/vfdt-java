package vfdt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfdt.ml.Classifier;
import vfdt.ml.TrainMethod;
import vfdt.tree.TreeMaker;
import vfdt.tree.VFDT;
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
        String[] exp = {
                "covertype",    // 0
                "iris",         // 1
                "transfusion",  // 2
                "seeds",        // 3
                "sdd",          // 4
                "ecoli",        // 5
                "htru2",        // 6
                "wifi",         // 7
        };
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
                "dt-a4-k2-n1000",           // 0
                "dt-a6-k2-n1000",           // 1
                "dt-a8-k2-n1000",           // 2
                "dt-a10-k2-n1000",          // 3
                "dt-a6-k4-n1000",           // 4
                "dt-a8-k4-n1000",           // 5
                "dt-a10-k4-n1000",          // 6
                "dt-a8-k6-n1000",           // 7
                "dt-a10-k6-n1000",          // 8
                "dt-a8-k8-n1000",           // 9
                "dt-a10-k8-n1000",          // 10
                "dt-a10-k10-n1000",         // 11
        };
        for (int i = 0; i < dt_exp.length; i++) dt_exp[i] = "dt/" + dt_exp[i];
        String[] norm_exp = {
                "normal-a4-k2-n1000",        // 0
                "normal-a6-k2-n1000",        // 1
                "normal-a8-k2-n1000",        // 2
                "normal-a10-k2-n1000",       // 3
                "normal-a6-k4-n1000",        // 4
                "normal-a8-k4-n1000",        // 5
                "normal-a10-k4-n1000",       // 6
                "normal-a8-k6-n1000",       // 7
                "normal-a10-k6-n1000",      // 8
                "normal-a8-k8-n1000",       // 9
                "normal-a10-k8-n1000",      // 10
                "normal-a10-k10-n1000",     // 11
        };
        for (int i = 0; i < norm_exp.length; i++) norm_exp[i] = "normal/" + norm_exp[i];

        String paramFileName = "params/" + exp[7] + ".json";
        paramFileName = paramFileName.replaceFirst("^~", System.getProperty("user.home"));
        logger.info("Parameter file: " + paramFileName);
        Config config = new Config(paramFileName);
        TrainMethod trainMethod = (TrainMethod) config.getParam("trainMethod");

        long startTime = System.currentTimeMillis();
        List<Pair<Classifier, Double>> results = trainMethod.evaluate();
//        System.out.println(((VFDT)results.get(0).getFirst()).print(true));
        long endTime = System.currentTimeMillis();
        logger.info("Training time: " + (endTime - startTime) / 1e3 + " seconds");
        logger.traceExit();
    }

    static void synthesize() throws IOException {
        Integer numAttributes = 10;
        Integer numClasses = 10;
        Double minAttValue = 0.;
        Double maxAttValue = 1.;
        Integer minHeight = 3;
        Integer maxHeight = 10;
        Double[] leafProbs = {0.1, 0.15, 0.2, 0.25};
        long numInstances = 1000;
        long[] seeds = {0, 1, 2};
        for (Double leafProb : leafProbs) {
            for (long seed : seeds) {
                long seedCorrected = (long) ((seed + 1) * (100 * leafProb));
                String datasetName = String.format("dt-a%d-k%d-p(%.2f)-s%d-n%d",
                        numAttributes, numClasses,
                        leafProb, seed, numInstances);
                Random rand = new Random(seedCorrected);
                TreeMaker.SimpleDecisionTree tree = TreeMaker.createTree(rand,
                        numAttributes, numClasses, minAttValue, maxAttValue,
                        minHeight, maxHeight, leafProb);
                tree.getDatasetInfo().setDatasetName(datasetName);
                String filePath = "/home/mehran/data/dt/" + datasetName + ".arff";
                TreeMaker.generateDataset(rand, tree, minAttValue, maxAttValue,
                        filePath, numInstances);
            }
        }
    }
}
