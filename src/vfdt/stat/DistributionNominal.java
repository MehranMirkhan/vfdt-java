package vfdt.stat;

import java.util.Hashtable;

/**
 * Nominal distribution.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class DistributionNominal implements Distribution {
    protected Hashtable<String, Integer> counts;

    public DistributionNominal() {
        reset();
    }

    public void add(String value) {
        Integer count = counts.getOrDefault(value, 0);
        counts.put(value, count + 1);
    }

    public void reset() {
        counts = new Hashtable<>();
    }

    public Counts getCounts() {
        return new Counts(counts.values());
    }

    public Integer[] split() {
        return counts.values().toArray(new Integer[0]);
    }
}
