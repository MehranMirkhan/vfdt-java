package test.measure.impurity;

import org.junit.Assert;
import org.junit.Test;
import vfdt.measure.impurity.Impurity;
import vfdt.measure.impurity.InformationEntropy;
import vfdt.measure.Counts;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class TestInformationEntropy {
    @Test
    public void correctness() {
        Counts   c = new Counts(4., 3., 2., 1.);
        Impurity im = new InformationEntropy();
        try {
            double result = im.measure(c);
            double expect = 1.2798542258;
            Assert.assertEquals(expect, result, 1e-8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
