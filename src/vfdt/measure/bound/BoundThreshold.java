package vfdt.measure.bound;

import vfdt.data.AttributeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Threshold-based boundary.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class BoundThreshold extends Bound {
    public AttributeInfo isSplitNeeded(HashMap<AttributeInfo, Double> gains, int numData) {
        Pair<Map.Entry<AttributeInfo, Double>> topTwo = getTopTwo(gains);
        Double threshold = getThreshold(numData);
        if (topTwo.first.getValue() - topTwo.second.getValue() > threshold)
            return topTwo.first.getKey();
        else
            return null;
    }

    public abstract Double getThreshold(int numData);
}
