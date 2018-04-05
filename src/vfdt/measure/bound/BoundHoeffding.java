package vfdt.measure.bound;

/**
 * Hoeffding bound.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public class BoundHoeffding extends BoundThreshold {
    private final Double delta;
    private final Double R;
    private final Double tieBreak;
    private final Double minGain;

    public BoundHoeffding(Double delta, Double R, Double tieBreak, Double minGain) {
        this.delta = delta;
        this.R = R;
        this.tieBreak = tieBreak;
        this.minGain = minGain;
    }

    @Override
    public Double getThreshold(int numData) {
        return R * Math.sqrt(Math.log(1 / delta) / (2 * numData));
    }

    @Override
    protected Double getTieBreak() {
        return tieBreak;
    }

    public Double getMinGain() {
        return minGain;
    }
}
