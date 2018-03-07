package vfdt.stat;

import vfdt.tree.Decision;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class SplitterGaussian implements Splitter {
    private AttributeStatisticsGaussian asn;

    public SplitterGaussian(AttributeStatisticsGaussian asn) {
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
