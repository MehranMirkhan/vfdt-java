package test.measure.gain;

import org.junit.Assert;
import org.junit.Test;
import vfdt.measure.gain.GainBase;
import vfdt.measure.impurity.MisclassificationError;
import vfdt.measure.Counts;
import vfdt.measure.gain.Split;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class TestGainBase {
    @Test
    public void testCorrectness() {
        Counts original = new Counts(4., 3., 2., 1.);
        Counts[] branches = new Counts[]{
                new Counts(4., 2., 0., 1.),
                new Counts(0., 1., 2., 0.)
        };
        Split split = new Split(original, branches);
        try {
            double result = (new GainBase(new MisclassificationError())).measure(split);
            double expect = 0.2;
            Assert.assertEquals(expect, result, 1e-12);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
