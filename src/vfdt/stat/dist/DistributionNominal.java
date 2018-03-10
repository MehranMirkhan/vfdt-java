package vfdt.stat.dist;

import vfdt.measure.Counts;

/**
 * Nominal distribution.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class DistributionNominal implements Distribution {
    private final String[] values;
    private final Counts   counts;

    public DistributionNominal(String[] values) {
        this.values = values;
        counts = new Counts(values.length);
    }

    public void add(String value) {
        int index = getIndexOfValue(value);
        counts.increment(index);
    }

    private int getIndexOfValue(String value) {
        for (int i = 0; i < values.length; i++)
            if (values[i].equals(value))
                return i;
        return -1;
    }

    public Counts getCounts() {
        return counts;
    }

    public Double[] split() {
        return counts.getCounts();
    }

    public int getNumValues() {
        return values.length;
    }
}
