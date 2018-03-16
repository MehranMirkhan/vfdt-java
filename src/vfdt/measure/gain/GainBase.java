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
    private final double minBranchFrac;

    public GainBase(Impurity im, double minBranchFrac) {
        this.im = im;
        this.minBranchFrac = minBranchFrac;
    }

    @Override
    public double measure(Split split) throws Exception {
        double   g          = im.measure(split.getOriginal());
        Counts[] branches   = split.getBranches();
        int      n_branches = branches.length;
        double[] gi         = new double[n_branches];
        int[]    ni         = new int[n_branches];
        int      n          = 0;
        double   result     = 0;
        for (int i=0; i < n_branches; i++) {
            ni[i] = branches[i].sum();
            n += ni[i];
        }
        int healthyBranches = 0;
        for (int i=0; i < n_branches; i++) {
            double p = (double) ni[i] / n;
            if (p >= minBranchFrac)
                healthyBranches += 1;
        }
        if (healthyBranches < 2)
            return Double.NEGATIVE_INFINITY;
        for (int i = 0; i < n_branches; i++) {
            gi[i] = im.measure(branches[i]);
            result += ni[i] * gi[i];
        }
        result = g - result / n;
        return result;
    }
}
