package vfdt.stat;

import vfdt.data.Attribute;
import vfdt.tree.Decision;

/**
 * Stores the information about an attribute from the arrived data for each class.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public abstract class AttributeStatistics {
    protected Splitter splitter;

    public Splitter getSplitter() {
        return splitter;
    }

    public void setSplitter(Splitter splitter) {
        this.splitter = splitter;
    }

    public abstract void update(Attribute att, int label);

    public Double getSplitGain() {
        return splitter.getSplitGain();
    }

    public Decision getDecision() {
        return splitter.getDecision();
    }
}
