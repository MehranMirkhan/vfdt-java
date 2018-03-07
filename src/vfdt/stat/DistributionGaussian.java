package vfdt.stat;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * Gaussian distribution.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class DistributionGaussian implements Distribution {
    protected int numData = 0;
    protected double sum = 0, sumSq = 0;
    protected double minVariance = 1e-6;
    protected double mean;
    protected double var;                       // Variance
    protected double std;                       // Standard deviation
    protected boolean isStatValid = false;      // Whether mean, var and std are valid or should be updated

    public void add(Double value) {
        sum += value;
        sumSq += value * value;
        numData += 1;
        isStatValid = false;
    }

    public void reset() {
        numData = 0;
        sum = 0;
        sumSq = 0;
        isStatValid = false;
    }

    public void update() {
        if (!isStatValid) {
            if (numData <= 0)
                throw new ArithmeticException("Divide by zero: numData = " + numData);
            mean = sum / numData;
            var = sumSq / numData - mean * mean;
            if (var < minVariance)
                var = minVariance;
            std = Math.sqrt(var);
            isStatValid = true;
        }
    }

    public Double[] split(Double value) {
        update();
        NormalDistribution normal = new NormalDistribution(mean, std);
        double left = normal.cumulativeProbability(value) * numData;
        double right = numData - left;
        return new Double[]{left, right};
    }

    public int getNumData() {
        return numData;
    }

    public double getSum() {
        return sum;
    }

    public double getSumSq() {
        return sumSq;
    }

    public double getMean() {
        return mean;
    }

    public double getVar() {
        return var;
    }

    public double getStd() {
        return std;
    }

    public boolean isStatValid() {
        return isStatValid;
    }
}
