package test.measure.bound;

import org.junit.Test;
import vfdt.measure.bound.Bound;
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
        Double delta = 1e-6;
        int numClasses = 3;
        Double R = Math.log(numClasses) / Math.log(2);
        Double tieBreak = 0.025;

        BoundHoeffding bound1 = new BoundHoeffding(delta, R, tieBreak);
        BoundGini bound2 = new BoundGini(delta, tieBreak, numClasses);
        BoundMisclassification bound3 = new BoundMisclassification(delta, tieBreak);

        int[] numData = {10, 100, 1000, 10000};
        for (int n : numData) {
//            System.out.println(
//                    "N=" + n +
//                    " / BH=" + bound1.getThreshold(n) +
//                    " / BG=" + bound2.getThreshold(n) +
//                    " / BM=" + bound3.getThreshold(n));
        }
    }
}
