package vfdt.stat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class Counts {
    private Integer[] counts;

    public Counts(Integer... counts) {
        this.counts = counts;
    }

    public Counts(Collection<Integer> counts) {
        this.counts = counts.toArray(new Integer[0]);
    }

    public Counts(Integer n) {
        counts = new Integer[n];
    }

    public Integer[] getCounts() {
        return counts;
    }

    public Integer getCount(int index) {
        return counts[index];
    }

    public void setCount(int index, Integer value) {
        counts[index] = value;
    }

    public void increment(int index) {
        counts[index] += 1;
    }

    public int sum() {
        int sum = 0;
        for (int c : counts)
            sum += c;
        return sum;
    }

    @Override
    public String toString() {
        return Arrays.toString(counts);
    }
}
