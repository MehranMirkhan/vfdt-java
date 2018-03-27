package vfdt.measure.gain;

import vfdt.measure.impurity.Impurity;
import vfdt.measure.Counts;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class GainBase implements Gain {
    private final Impurity im;
    private final double   minBranchFrac;

    public GainBase(Impurity im, double minBranchFrac) {
        this.im = im;
        this.minBranchFrac = minBranchFrac;
    }

    @Override
    public Double measure(Split split) {
        try {
            double   g          = im.measure(split.getOriginal());
            Counts[] branches   = split.getBranches();
            int      n_branches = branches.length;
            double[] G          = new double[n_branches];
            double[] N          = new double[n_branches];
            double   n          = split.getOriginal().sum();
            double   result     = 0;
            for (int i = 0; i < n_branches; i++)
                N[i] = branches[i].sum();
            int healthyBranches = 0;
            for (int i = 0; i < n_branches; i++) {
                double p = N[i] / n;
                if (p >= minBranchFrac)
                    healthyBranches++;
            }
            if (healthyBranches < 2)
                return 0.;
            for (int i = 0; i < n_branches; i++) {
                G[i] = im.measure(branches[i]);
                result += N[i] * G[i];
            }
            result = g - result / n;
            return result;
        } catch (ArithmeticException e) {
            return null;
        }
    }
}
