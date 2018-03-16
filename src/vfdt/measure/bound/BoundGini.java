package vfdt.measure.bound;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class BoundGini extends BoundThreshold {
    private final Double delta;
    private final Double tieBreak;
    private final int    numClasses;

    public BoundGini(Double delta, Double tieBreak, int numClasses) {
        this.delta = delta;
        this.tieBreak = tieBreak;
        this.numClasses = numClasses;
    }

    @Override
    public Double getThreshold(int numData) {
        NormalDistribution norm      = new NormalDistribution(0, 1);
        Double             z         = norm.inverseCumulativeProbability(1 - delta);
        int                K         = numClasses;
        int                nominator = 10 * K * K - 16 * K + 8;
        return z * Math.sqrt((double) nominator / numData);
    }

    @Override
    protected Double getTieBreak() {
        return tieBreak;
    }
}
