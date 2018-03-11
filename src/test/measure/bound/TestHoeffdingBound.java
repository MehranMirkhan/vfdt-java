package test.measure.bound;

import org.junit.Assert;
import org.junit.Test;
import vfdt.measure.bound.BoundHoeffding;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class TestHoeffdingBound {
    @Test
    public void testCorrectness() {
        double delta = 0.05;
//        double numClasses = 6;
//        double R = Math.log(numClasses);
        double         R        = 2;
        int            numData  = 100;
        Double         tieBreak = 0.01;
        BoundHoeffding bound    = new BoundHoeffding(delta, R, tieBreak);
        double         result   = bound.getThreshold(numData);
        double         expect   = 0.244774683;
        Assert.assertEquals(expect, result, 1e-6);
    }
}
