package test.measure.bound;

import org.junit.Test;
import vfdt.measure.bound.BoundGini;
import vfdt.measure.bound.BoundHoeffding;
import vfdt.measure.bound.BoundMisclassification;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class TestCompareBounds {
    @Test
    public void testCompare() {
        Double delta = 1e-7;
        int numClasses = 10;
        Double R = Math.log(numClasses) / Math.log(2);
        Double tieBreak = 0.025;
        Double minGain = null;

        BoundHoeffding bound1 = new BoundHoeffding(delta, R, tieBreak, minGain);
        BoundGini bound2 = new BoundGini(delta, tieBreak, numClasses, minGain);
        BoundMisclassification bound3 = new BoundMisclassification(delta, tieBreak, minGain);

        int[] numData = {10, 50, 100, 500, 1000, 5000, 10000};
        for (int n : numData) {
//            System.out.println(
//                    "N=" + n +
//                    " / BH=" + bound1.getThreshold(n) +
//                    " / BG=" + bound2.getThreshold(n) +
//                    " / BM=" + bound3.getThreshold(n));
        }
    }
}
