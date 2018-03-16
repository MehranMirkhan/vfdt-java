package vfdt.measure.bound;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class BoundMisclassification extends BoundThreshold {
    private final Double delta;
    private final Double tieBreak;

    public BoundMisclassification(Double delta, Double tieBreak) {
        this.delta = delta;
        this.tieBreak = tieBreak;
    }

    @Override
    public Double getThreshold(int numData) {
        NormalDistribution norm = new NormalDistribution(0, 1);
        Double             z    = norm.inverseCumulativeProbability(1 - delta);
        return z * Math.sqrt(1.0 / (2 * numData));
    }

    @Override
    protected Double getTieBreak() {
        return tieBreak;
    }
}
