package vfdt.stat;

import vfdt.tree.Decision;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class SplitterNominal implements Splitter {
    private AttributeStatisticsNominal asn;

    public SplitterNominal(AttributeStatisticsNominal asn) {
        this.asn = asn;
    }

    @Override
    public Double getSplitGain() {
        // todo
        return null;
    }

    @Override
    public Decision getDecision() {
        // todo
        return null;
    }
}
