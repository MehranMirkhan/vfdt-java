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
    protected Impurity im;

    public GainBase(Impurity im) {
        this.im = im;
    }

    @Override
    public double measure(Split split) throws Exception {
        double g = im.measure(split.getOriginal());
        Counts[] branches = split.getBranches();
        int n_branches = branches.length;
        double[] gi = new double[n_branches];
        int[] ni = new int[n_branches];
        int n = 0;
        double result = 0;
        for (int i=0; i<gi.length; i++) {
            gi[i] = im.measure(branches[i]);
            ni[i] = branches[i].sum();
            n += ni[i];
            result += ni[i] * gi[i];
        }
        result = g - result / n;
        return result;
    }
}
