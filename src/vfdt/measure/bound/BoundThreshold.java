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
        Pair<Map.Entry<AttributeInfo, Double>> topTwo     = getTopTwo(gains);
        Double                                 threshold  = getThreshold(numData);
        Double                                 firstValue = 0., secondValue = 0.;
        if (topTwo.first == null && topTwo.second == null)
            throw new NullPointerException("Bound received no attribute.");
        if (topTwo.first != null)
            firstValue = topTwo.first.getValue();
        if (topTwo.second != null)
            secondValue = topTwo.second.getValue();
        if (firstValue - secondValue > threshold)
            return topTwo.first.getKey();
        else
            return null;
    }

    protected abstract Double getThreshold(int numData);
}
