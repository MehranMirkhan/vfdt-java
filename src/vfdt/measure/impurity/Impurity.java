package vfdt.measure.impurity;

import vfdt.stat.Counts;

/**
 * Interface of classes
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public interface Impurity {
    public double measure(Counts p) throws Exception;
}
