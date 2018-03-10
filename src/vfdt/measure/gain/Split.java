package vfdt.measure.gain;

import vfdt.measure.Counts;

/**
 * Stores the information about a split.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class Split {
    private final Counts   original;
    private final Counts[] branches;

    public Split(Counts original, Counts[] branches) {
        this.original = original;
        this.branches = branches;
    }

    public Counts getOriginal() {
        return original;
    }

    public Counts[] getBranches() {
        return branches;
    }
}
