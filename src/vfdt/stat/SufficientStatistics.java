package vfdt.stat;

import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.tree.Decision;

import java.util.Collection;
import java.util.HashMap;

/**
 * The sufficient statistics stored in a node.
 * This class keeps track of how many data have arrived for each attribute and class.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class SufficientStatistics {
    private HashMap<AttributeInfo, AttributeStatistics> attStats;
    private DatasetInfo datasetInfo;

    public SufficientStatistics(DatasetInfo datasetInfo) {
        this.datasetInfo = datasetInfo;
        attStats = new HashMap<>();
    }

    public SufficientStatistics(DatasetInfo datasetInfo, Collection<AttributeInfo> availableAtts) throws Exception {
        this(datasetInfo);
        int numClasses = datasetInfo.getNumClasses();
        for (AttributeInfo attInfo : availableAtts) {
            AttributeStatistics attStat;
            switch (attInfo.getType()) {
                case NOMINAL:
                    attStat = new AttributeStatisticsNominal(numClasses, attInfo.getValues());
                    break;
                case NUMERICAL:
                    attStat = new AttributeStatisticsGaussian(numClasses);
                    break;
                default:
                    throw new Exception("Attribute type not recognized: " + attInfo.getType());
            }
            attStats.put(attInfo, attStat);
        }
    }

    public void update(Instance instance) {
        // todo
    }

    public boolean checkSplit(Splitter splitter) {
        // todo
        return false;
    }

    public Decision getDecision() {
        // todo
        return null;
    }
}
