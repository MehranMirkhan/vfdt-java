package vfdt.measure.bound;

import vfdt.data.AttributeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Hoeffding bound.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public class BoundHoeffding extends Bound {
    protected Double delta, R;

    public BoundHoeffding(Double delta, Double r) {
        this.delta = delta;
        R = r;
    }

    @Override
    public AttributeInfo isSplitNeeded(HashMap<AttributeInfo, Double> gains, int numData) {
        Pair<Map.Entry<AttributeInfo, Double>> topTwo = getTopTwo(gains);
        return null;
    }
}
