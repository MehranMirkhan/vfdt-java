package vfdt.stat.attstat;

import vfdt.data.Attribute;
import vfdt.stat.dist.DistributionGaussian;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class AttStatGaussian implements AttStat {
    private final DistributionGaussian[] classDist;
    private Double minValue = Double.POSITIVE_INFINITY;
    private Double maxValue = Double.NEGATIVE_INFINITY;

    public AttStatGaussian(int numClasses) {
        classDist = new DistributionGaussian[numClasses];
        for (int i = 0; i < numClasses; i++)
            classDist[i] = new DistributionGaussian();
    }

    public DistributionGaussian[] getClassDist() {
        return classDist;
    }

    public Double getMinValue() {
        return minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    @Override
    public void update(Attribute att, Attribute label) {
        Double value = (Double) att.getValue();
        if (value > maxValue)
            maxValue = value;
        if (value < minValue)
            minValue = value;
        classDist[label.getValueIndex()].add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for (DistributionGaussian dist : classDist) {
            sb.append("-- c" + counter + ": " + dist.toString() + "\n");
            counter++;
        }
        sb.append("minVal=" + minValue + "\n");
        sb.append("maxVal=" + maxValue + "\n");
        return sb.toString();
    }
}
