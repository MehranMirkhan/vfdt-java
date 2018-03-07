package vfdt.stat;

import vfdt.data.AttributeInfo;

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
    private HashMap distributions;

    public SufficientStatistics() {
        distributions = new HashMap<>();
    }

    public SufficientStatistics(AttributeInfo... availableAtts) throws Exception {
        distributions = new HashMap<>();
        for (AttributeInfo attInfo : availableAtts) {
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
            distributions.put(attInfo, attStat);
        }
    }
}
