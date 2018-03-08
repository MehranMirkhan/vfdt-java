package vfdt.measure.impurity;

import vfdt.measure.Counts;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class GiniIndex extends ImpurityBase {
    @Override
    public double measure(Counts p) throws Exception {
        double[] q = this.preprocess(p);
        double result = 0;
        for (double v : q) {
            result += v * v;
        }
        return 1 - result;
    }
}
