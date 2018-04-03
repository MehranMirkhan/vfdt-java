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
        Pair<Map.Entry<AttributeInfo, Double>> topTwo     = getTopTwo(gains);
        Double                                 threshold  = getThreshold(numData);
        Double                                 tiebreak   = getTieBreak();
        AttributeInfo a1 = topTwo.first.getKey();
        Double g1 = topTwo.first.getValue();
        AttributeInfo a2 = topTwo.second.getKey();
        Double g2 = topTwo.second.getValue();
        if (a1 == null || g1 == 0.)
            return null;
        boolean isSplitNeeded = g1 - g2 > threshold;
        isSplitNeeded = isSplitNeeded || threshold < tiebreak;
        if (isSplitNeeded) {
            Logger logger = LogManager.getLogger();
            DecimalFormat df = new DecimalFormat("#.000");
            logger.debug("(g1={})(g2={})", df.format(g1), df.format(g2));
//            Logger.append("Gains: ");
//            for (Map.Entry<AttributeInfo, Double> entry : gains.entrySet()) {
//                AttributeInfo attInfo = entry.getKey();
//                Double gain = entry.getValue();
//                if (attInfo == null || gain == null)
//                    continue;
//                String attName = attInfo.getName();
//                String g = Logger.df.format(gain);
//                Logger.append("(" + attName + ", " + g + ") ");
//            }
//            Logger.append("\n");
//            Logger.append("First: (" + a1.getName() + ", " + g1 + ") \n");
//            if (a2 != null)
//                Logger.append("Second: (" + a2.getName() + ", " + g2 + ") \n");
            return a1;
        }
        else
            return null;
    }

    public abstract Double getThreshold(int numData);

    protected abstract Double getTieBreak();
}
