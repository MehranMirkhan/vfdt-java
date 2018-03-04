package vfdt.stat;

import java.util.Arrays;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class Counts {
    private int[] counts;

    public Counts(int... counts) {
        this.counts = counts;
    }

    public Counts(int n) {
        counts = new int[n];
    }

    public int[] getCounts() {
        return counts;
    }

    public int getCount(int index) {
        return counts[index];
    }

    public void setCount(int index, int value) {
        counts[index] = value;
    }

    public void increment(int index) {
        counts[index] += 1;
    }

    @Override
    public String toString() {
        return Arrays.toString(counts);
    }
}
