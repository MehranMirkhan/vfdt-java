package vfdt.measure.bound;

import vfdt.data.AttributeInfo;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Checks whether split is needed.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class Bound {

    protected class Pair<T> {
        T first, second;

        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }
    }

    protected Pair<Map.Entry<AttributeInfo, Double>> getTopTwo(HashMap<AttributeInfo, Double> gains) {
        AttributeInfo a1 = null, a2 = null;
        Double        g1 = Double.NEGATIVE_INFINITY, g2 = Double.NEGATIVE_INFINITY;
        for (Map.Entry<AttributeInfo, Double> entry : gains.entrySet()) {
            AttributeInfo attInfo = entry.getKey();
            Double gain = entry.getValue();
            if (gain > g1) {
                g2 = g1;
                g1 = gain;
                a2 = a1;
                a1 = attInfo;
            } else if (gain > g2) {
                g2 = gain;
                a2 = attInfo;
            }
        }
        Map.Entry<AttributeInfo, Double> e1 = new AbstractMap.SimpleEntry<AttributeInfo, Double>(a1, g1);
        Map.Entry<AttributeInfo, Double> e2 = new AbstractMap.SimpleEntry<AttributeInfo, Double>(a2, g2);
        return new Pair<Map.Entry<AttributeInfo, Double>>(e1, e2);
    }

    public abstract AttributeInfo isSplitNeeded(HashMap<AttributeInfo, Double> gains, int numData);
}