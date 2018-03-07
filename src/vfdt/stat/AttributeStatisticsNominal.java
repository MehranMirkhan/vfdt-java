package vfdt.stat;

import vfdt.data.Attribute;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class AttributeStatisticsNominal extends AttributeStatistics {
    protected DistributionNominal[] classDist;

    public AttributeStatisticsNominal(int numClasses, String[] values) {
        classDist = new DistributionNominal[numClasses];
        for (int i=0; i<numClasses; i++)
            classDist[i] = new DistributionNominal(values);
        setSplitter(new SplitterNominal(this));
    }

    @Override
    public void update(Attribute att, int label) {
        classDist[label].add((String) att.getValue());
    }
}
