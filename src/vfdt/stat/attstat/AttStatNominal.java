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
public class AttStatNominal implements AttStat {
    protected DistributionNominal[] classDist;

    public AttStatNominal(int numClasses, String[] values) {
        classDist = new DistributionNominal[numClasses];
        for (int i = 0; i < numClasses; i++)
            classDist[i] = new DistributionNominal(values);
    }

    public DistributionNominal[] getClassDist() {
        return classDist;
    }

    @Override
    public void update(Attribute att, Attribute label) {
        String value = (String) att.getValue();
        classDist[label.getValueIndex()].add(value);
    }
}
