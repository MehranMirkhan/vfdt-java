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

    public BoundHoeffding(Double delta, Double R) {
        this.delta = delta;
        this.R = R;
    }

    @Override
    public Double getThreshold(int numData) {
        return R * Math.sqrt(Math.log(1 / delta) / (2 * numData));
    }
}
