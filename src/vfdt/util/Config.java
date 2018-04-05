package vfdt.util;

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
import vfdt.ml.*;
import vfdt.stat.SuffStatFactory;
import vfdt.stat.SuffStatFactoryBase;
import vfdt.stat.attstat.AttStatFactoryBase;
import vfdt.stat.splitter.SplitterFactoryBase;
import vfdt.tree.SplitPolicy;
import vfdt.tree.VFDT;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.*;
import java.util.HashMap;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 17
 */
public class Config {
    private final String paramFileName;
    private HashMap<String, Object> config;

    public Config(String paramFileName) throws Exception {
        this.paramFileName = paramFileName;
        config = new HashMap<>();
        loadParams();
    }

    public Object getParam(String key) {
        return config.get(key);
    }

    public void loadParams() throws Exception {
        InputStream stream = new FileInputStream(paramFileName);
        JSONObject obj = new JSONObject(new JSONTokener(stream));

        DatasetInfo datasetInfo = getDatasetInfo(obj);
        config.put("datasetInfo", datasetInfo);

        // hyper parameters
        Integer gracePeriod = null;
        Double delta = null;
        Double R, tieBreak = null;
        Bound bound = null;
        Double minGain = null;
        if (obj.has("gracePeriod"))
            gracePeriod = obj.getInt("gracePeriod");
        int numClasses = datasetInfo.getNumClasses();
        if (obj.has("tieBreak"))
            tieBreak = obj.getDouble("tieBreak");
        if (obj.has("delta"))
            delta = obj.getDouble("delta");
        if (obj.has("minGain"))
            minGain = obj.getDouble("minGain");
        switch (obj.getString("bound").toLowerCase()) {
            case "hoeffding":
                R = java.lang.Math.log(numClasses) / java.lang.Math.log(2);
                bound = new BoundHoeffding(delta, R, tieBreak, minGain);
                break;
            case "gini":
                bound = new BoundGini(delta, tieBreak, numClasses, minGain);
                break;
            case "misclassification":
                bound = new BoundMisclassification(delta, tieBreak, minGain);
                break;
        }
        int maxHeight = obj.getInt("maxHeight");
        Impurity impurity = null;
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
        Gain gain = new GainBase(impurity, minBranchFrac);
        Integer numBins = null;
        if (obj.has("numBins"))
            numBins = obj.getInt("numBins");
        String splitter = obj.getString("splitter");

        SplitPolicy splitPolicy = new SplitPolicy(gracePeriod, bound, maxHeight);
        SuffStatFactory suffStatFactory = new SuffStatFactoryBase(
                new AttStatFactoryBase(datasetInfo),
                new SplitterFactoryBase(gain, numBins, splitter)
        );

        ClassifierFactory classifierFactory = new ClassifierFactory() {
            @Override
            public Classifier build() throws Exception {
                return new VFDT(datasetInfo, splitPolicy, suffStatFactory);
            }
        };
        config.put("classifierFactory", classifierFactory);

        TrainMethod trainMethod = getTrainMethod(obj, classifierFactory, datasetInfo);
        config.put("trainMethod", trainMethod);

        stream.close();
    }

    private DatasetInfo getDatasetInfo(JSONObject obj) throws IOException {
        JSONObject di_obj = obj.getJSONObject("datasetInfo");
        String fileName = di_obj.getString("fileName");
        int classIndex = di_obj.getInt("classIndex");
        int numData = di_obj.getInt("numData");
        DatasetReader reader = new ArffReader(fileName);
        DatasetInfo datasetInfo = reader.analyze();
        datasetInfo.setClassIndex(classIndex);
        datasetInfo.setNumData(numData);
        return datasetInfo;
    }

    private TrainMethod getTrainMethod(JSONObject obj, ClassifierFactory classifierFactory, DatasetInfo datasetInfo) {
        TrainMethod tm = null;
        JSONObject sc_obj = obj.has("stopCriterion") ? obj.getJSONObject("stopCriterion") : null;
        StopCriterion stopCriterion = sc_obj != null ? new StopCriterion() : null;

        JSONObject tm_obj = obj.getJSONObject("trainMethod");
        int numEpochs = tm_obj.getInt("numEpochs");
        String method = tm_obj.getString("method");
        switch (method) {
            case "same":
                tm = new TrainMethodSame(classifierFactory, datasetInfo,
                        tm_obj.getString("fileName"),
                        numEpochs, stopCriterion);
                break;
            case "split":
                tm = new TrainMethodSplit(classifierFactory, datasetInfo,
                        tm_obj.getString("fileName"), tm_obj.getDouble("percent"),
                        numEpochs, stopCriterion);
                break;
            case "separate":
                tm = new TrainMethodSeparate(classifierFactory, datasetInfo,
                        tm_obj.getString("trainFile"), tm_obj.getString("testFile"),
                        numEpochs, stopCriterion);
                break;
            case "kfold":
                tm = new TrainMethodKFold(classifierFactory, datasetInfo,
                        tm_obj.getString("fileName"), tm_obj.getInt("k"),
                        numEpochs, stopCriterion);
                break;
        }
        return tm;
    }
}
