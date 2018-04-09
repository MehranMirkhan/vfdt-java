package vfdt.stat.dist;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.lang.Math;
import java.text.DecimalFormat;

/**
 * Gaussian distribution.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class DistributionGaussian implements Distribution {
    private       int    numData     = 0;
    private       double sum         = 0;
    private       double sumSq       = 0;
    private final double minVariance = 1e-6;
    private double mean;
    private double var;                       // Variance
    private double std;                       // Standard deviation
    private boolean isStatValid = false;      // Whether mean, var and std are valid or should be updated

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

    private void update() {
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
        if (numData > 0) {
            update();
            NormalDistribution normal = new NormalDistribution(mean, std);
            double left = normal.cumulativeProbability(value) * getNumData();
            double right = getNumData() - left;
            return new Double[]{left, right};
        } else
            return null;
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
        update();
        return mean;
    }

    public double getVar() {
        update();
        return var;
    }

    public double getStd() {
        update();
        return std;
    }

    public void setNumData(int numData) {
        this.numData = numData;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public void setVar(double var) {
        this.var = var;
    }

    public void setStd(double std) {
        this.std = std;
    }

    public void setStatValid(boolean statValid) {
        isStatValid = statValid;
    }

    public boolean isStatValid() {
        return isStatValid;
    }

    public static Double[] intersect(DistributionGaussian d1, DistributionGaussian d2) {
        d1.update();
        d2.update();
        double mean1 = d1.getMean();
        double mean2 = d2.getMean();
        double var1  = d1.getVar();
        double var2  = d2.getVar();
        double std1  = d1.getStd();
        double std2  = d2.getStd();
        double n1    = d1.getNumData();
        double n2    = d2.getNumData();
        return vfdt.util.Math.solveEq(
                1 / var1 - 1 / var2,
                -2 * (mean1 / var1 - mean2 / var2),
                mean1 * mean1 / var1 - mean2 * mean2 / var2 - 2 * Math.log((n1 * std2) / (n2 * std1)));
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.0000000");
        Double m = mean;
        Double s = std;
        return "{" +
                "mean=" + m +
                ", std=" + s +
                ", N=" + numData +
                '}';
    }
}
