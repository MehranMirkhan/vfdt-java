package vfdt.measure.gain;

import vfdt.measure.impurity.Impurity;
import vfdt.stat.Split;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public interface Gain {
    public double measure(Split split, Impurity im) throws Exception;
}
