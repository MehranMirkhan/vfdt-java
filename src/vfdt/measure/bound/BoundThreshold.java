package vfdt.measure.bound;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfdt.data.AttributeInfo;
//import vfdt.util.Logger;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Threshold-based boundary.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class BoundThreshold extends Bound {
    public AttributeInfo isSplitNeeded(LinkedHashMap<AttributeInfo, Double> gains, int numData) {
        Pair<Map.Entry<AttributeInfo, Double>> topTwo = getTopTwo(gains);
        Double threshold = getThreshold(numData);
        Double tiebreak = getTieBreak();
        Double minGain = getMinGain();
        AttributeInfo a1 = topTwo.first.getKey();
        Double g1 = topTwo.first.getValue();
        AttributeInfo a2 = topTwo.second.getKey();
        Double g2 = topTwo.second.getValue();
        if (a1 == null || g1 == 0.)
            return null;
        boolean isSplitNeeded = (g1 - g2) > threshold;
        if (tiebreak != null)
            isSplitNeeded = isSplitNeeded || threshold < tiebreak;
        if (minGain != null)
            isSplitNeeded = isSplitNeeded && g1 > minGain;
        if (isSplitNeeded) {
            Logger logger = LogManager.getLogger();
            DecimalFormat df = new DecimalFormat("#.000");
            logger.debug("(g1={})(g2={})", df.format(g1), df.format(g2));
            return a1;
        } else
            return null;
    }

    public abstract Double getThreshold(int numData);

    protected abstract Double getTieBreak();

    protected abstract Double getMinGain();
}
