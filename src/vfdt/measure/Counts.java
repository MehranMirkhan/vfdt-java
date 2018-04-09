package vfdt.measure;

import java.util.Arrays;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class Counts {
    private final Double[] counts;

    public Counts(Double... counts) {
        this.counts = counts;
    }

    public Counts(Integer n) {
        counts = new Double[n];
        for (int i = 0; i < n; i++)
            counts[i] = 0.;
    }

    public Double[] getCounts() {
        return counts;
    }

    public Double getCount(int index) {
        return counts[index];
    }

    public void setCount(int index, Double value) {
        counts[index] = value;
    }

    public void increment(int index) {
        counts[index] += 1;
    }

    public void add(int index, Double value) {
        counts[index] += value;
    }

    public double sum() {
        double sum = 0;
        for (Double c : counts)
            sum += c;
        return sum;
    }

    @Override
    public String toString() {
        return Arrays.toString(counts);
    }

    public Integer getIndexOfMax() {
        Double  maxValue = Double.NEGATIVE_INFINITY;
        Integer maxIndex = null;
        for (int index = 0; index < getCounts().length; index++) {
            Double value = getCount(index);
            if (value > maxValue) {
                maxValue = value;
                maxIndex = index;
            }
        }
        return maxIndex;
    }

    public Double getMax() {
        return getCount(getIndexOfMax());
    }
}
