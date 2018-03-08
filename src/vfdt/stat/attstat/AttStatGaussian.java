package vfdt.stat.attstat;

import vfdt.data.Attribute;
import vfdt.stat.splitter.SplitterGaussian;
import vfdt.stat.dist.DistributionGaussian;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class AttStatGaussian extends AttStat {
    protected DistributionGaussian[] classDist;
    protected Double minValue = Double.POSITIVE_INFINITY,
            maxValue = Double.NEGATIVE_INFINITY;

    public AttStatGaussian(int numClasses) {
        classDist = new DistributionGaussian[numClasses];
        for (int i = 0; i < numClasses; i++)
            classDist[i] = new DistributionGaussian();
    }

    @Override
    public void update(Attribute att, int label) {
        Double value = (Double) att.getValue();
        if (value > maxValue)
            maxValue = value;
        if (value < minValue)
            minValue = value;
        classDist[label].add(value);
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
}
