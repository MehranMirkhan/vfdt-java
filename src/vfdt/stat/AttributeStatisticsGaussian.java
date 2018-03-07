package vfdt.stat;

import vfdt.data.Attribute;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class AttributeStatisticsGaussian extends AttributeStatistics {
    protected DistributionGaussian[] classDist;

    public AttributeStatisticsGaussian(int numClasses) {
        classDist = new DistributionGaussian[numClasses];
        for (int i=0; i<numClasses; i++)
            classDist[i] = new DistributionGaussian();
        setSplitter(new SplitterGaussian(this));
    }

    @Override
    public void update(Attribute att, int label) {
        classDist[label].add((Double) att.getValue());
    }
}
