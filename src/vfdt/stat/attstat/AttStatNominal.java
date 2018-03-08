package vfdt.stat.attstat;

import vfdt.data.Attribute;
import vfdt.stat.splitter.SplitterNominal;
import vfdt.stat.dist.DistributionNominal;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class AttStatNominal extends AttStat {
    protected DistributionNominal[] classDist;

    public AttStatNominal(int numClasses, String[] values) {
        classDist = new DistributionNominal[numClasses];
        for (int i = 0; i < numClasses; i++)
            classDist[i] = new DistributionNominal(values);
    }

    @Override
    public void update(Attribute att, int label) {
        classDist[label].add((String) att.getValue());
    }

    public DistributionNominal[] getClassDist() {
        return classDist;
    }
}
