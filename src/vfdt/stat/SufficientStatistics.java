package vfdt.stat;

import vfdt.data.AttributeInfo;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * The sufficient statistics stored in a node.
 * This class keeps track of how many data have arrived for each attribute and class.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class SufficientStatistics {
    private Dictionary<String, AttributeStatistics> distributions;

    public SufficientStatistics() {
        distributions = new Hashtable<>();
    }

    public SufficientStatistics(AttributeInfo... availableAtts) throws Exception {
        distributions = new Hashtable<>();
        for (AttributeInfo attInfo : availableAtts) {
            String attName = attInfo.getName();
            AttributeStatistics attStat;
            switch (attInfo.getType()) {
                case NOMINAL:
                    attStat = null;      // todo: implement
                    break;
                case NUMERICAL:
                    attStat = null;      // todo: implement
                    break;
                default:
                    throw new Exception("Attribute type not recognized: " + attInfo.getType());
            }
            distributions.put(attName, attStat);
        }
    }
}
