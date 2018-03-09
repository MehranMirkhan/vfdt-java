package vfdt.tree;

import vfdt.measure.bound.Bound;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class SplitPolicy {
    private int gracePeriod;
    private Bound bound;
    private int maxHeight;

    public SplitPolicy(int gracePeriod, Bound bound, int maxHeight) {
        this.gracePeriod = gracePeriod;
        this.bound = bound;
        this.maxHeight = maxHeight;
    }

    public int getGracePeriod() {
        return gracePeriod;
    }

    public void setGracePeriod(int gracePeriod) {
        this.gracePeriod = gracePeriod;
    }

    public Bound getBound() {
        return bound;
    }

    public void setBound(Bound bound) {
        this.bound = bound;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
