package test.measure.impurity;

import org.junit.Assert;
import org.junit.Test;
import vfdt.measure.impurity.GiniIndex;
import vfdt.measure.impurity.Impurity;
import vfdt.stat.Counts;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class TestGiniIndex {
    @Test
    public void correctness() {
        Counts c = new Counts(4, 3, 2, 1);
        Impurity im = new GiniIndex();
        try {
            double result = im.measure(c);
            double expect = 0.7;
            Assert.assertEquals(expect, result, 1e-12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
