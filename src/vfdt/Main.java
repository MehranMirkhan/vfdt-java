package vfdt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfdt.ml.Classifier;
import vfdt.ml.TrainMethod;
import vfdt.util.Config;
import vfdt.util.Pair;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Logger logger = LogManager.getLogger();
        logger.traceEntry();
        String[] exp = {
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
                "covertype"                  // 12
        };
        String paramFileName = "params/" + exp[0] + ".json";
        paramFileName = paramFileName.replaceFirst("^~", System.getProperty("user.home"));
        logger.info("Parameter file: " + paramFileName);
        Config config = new Config(paramFileName);
        TrainMethod trainMethod = (TrainMethod) config.getParam("trainMethod");

        long startTime = System.currentTimeMillis();
        List<Pair<Classifier, Double>> results = trainMethod.evaluate();
        long endTime = System.currentTimeMillis();
        logger.info("Training time: " + (endTime - startTime) / 1e3 + " seconds");
        logger.traceExit();
    }
}
