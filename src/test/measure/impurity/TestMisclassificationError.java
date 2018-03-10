package test.measure.impurity;

import org.junit.Assert;
import org.junit.Test;
import vfdt.measure.impurity.Impurity;
import vfdt.measure.impurity.MisclassificationError;
import vfdt.measure.Counts;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class TestMisclassificationError {
    @Test
    public void correctness() {
        Counts   c  = new Counts(4., 3., 2., 1.);
        Impurity im = new MisclassificationError();
        try {
            double result = im.measure(c);
            double expect = 0.6;
            Assert.assertEquals(expect, result, 1e-12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
