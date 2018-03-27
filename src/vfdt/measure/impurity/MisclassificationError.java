package vfdt.measure.impurity;

import vfdt.measure.Counts;

import java.util.Arrays;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class MisclassificationError extends ImpurityBase {
    @Override
    public double measure(Counts p) throws ArithmeticException {
        double[] q = this.preprocess(p);
        return 1 - Arrays.stream(q).max().getAsDouble();
    }
}
