package test.measure.impurity;

import org.junit.Assert;
import org.junit.Test;
import vfdt.measure.impurity.ImpurityBase;
import vfdt.measure.Counts;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class TestImpurityBase {
    @Test
    public void testPreprocess() throws Exception {
        Counts p = new Counts(2., 1., 3., 4.);
        double[] q = {0.2, 0.1, 0.3, 0.4};
        ImpurityBase ib = new ImpurityBase() {
            @Override
            public double measure(Counts p) {
                return 0;
            }
        };
        Assert.assertArrayEquals(q, ib.preprocess(p), 1e-6);
    }

    @Test(expected = ArithmeticException.class)
    public void testPreprocessExceptionSum() throws Exception {
        Counts p = new Counts(1., 0., -1.);
        ImpurityBase ib = new ImpurityBase() {
            @Override
            public double measure(Counts p) {
                return 0;
            }
        };
        ib.preprocess(p);
    }

    @Test(expected = ArithmeticException.class)
    public void testPreprocessExceptionNValid() throws Exception {
        Counts p = new Counts(0., 0., 0.);
        ImpurityBase ib = new ImpurityBase() {
            @Override
            public double measure(Counts p) {
                return 0;
            }
        };
        ib.preprocess(p);
    }

    @Test(expected = ArithmeticException.class)
    public void testPreprocessExceptionValue() throws Exception {
        Counts p = new Counts(1., -2., 3.);
        ImpurityBase ib = new ImpurityBase() {
            @Override
            public double measure(Counts p) {
                return 0;
            }
        };
        ib.preprocess(p);
    }
}
