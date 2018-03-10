package vfdt.measure.impurity;

import vfdt.measure.Counts;

/**
 * Basic implementation of impurity measures.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public abstract class ImpurityBase implements Impurity {
    public double[] preprocess(Counts p) throws Exception {
        Exception ex      = new ArithmeticException("Impurity measure error. p = " + p.toString());
        double    sum     = 0;
        int       n_valid = 0;
        for (Double value : p.getCounts()) {
            sum += value;
            n_valid += value > 0 ? 1 : 0;
            if (value < 0)
                throw ex;
        }
        if (sum <= 0 || n_valid < 1)
            throw ex;
        double[] q = new double[n_valid];
        int      i = 0;
        for (Double value : p.getCounts()) {
            if (value > 0) {
                q[i] = value / sum;
                i += 1;
            }
        }
        return q;
    }
}
