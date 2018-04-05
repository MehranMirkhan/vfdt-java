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
    private Integer gracePeriod;
    private Bound bound;
    private Integer maxHeight;

    public SplitPolicy(Integer gracePeriod, Bound bound, Integer maxHeight) {
        this.gracePeriod = gracePeriod;
        this.bound = bound;
        this.maxHeight = maxHeight;
    }

    public Integer getGracePeriod() {
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

    public Integer getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}
