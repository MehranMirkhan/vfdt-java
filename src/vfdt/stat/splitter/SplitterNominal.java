package vfdt.stat.splitter;

import vfdt.data.Attribute;
import vfdt.measure.Counts;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.Split;
import vfdt.stat.attstat.AttStatNominal;
import vfdt.tree.Decision;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class SplitterNominal implements Splitter {
    private AttStatNominal asn;
    private Gain gain;

    public SplitterNominal(AttStatNominal asn, Gain gain) {
        this.asn = asn;
        this.gain = gain;
    }

    @Override
    public Double getSplitGain() throws Exception {
        int numValues  = asn.getClassDist()[0].getNumValues();
        int numClasses = asn.getClassDist().length;
        Counts original = new Counts(numClasses);
        for (int c=0; c<numClasses; c++) {
            original.add(c, (double) asn.getClassDist()[c].getCounts().sum());
        }
        Counts[] branches   = new Counts[numValues];
        for (int i=0; i<numValues; i++) {
            branches[i] = new Counts(numClasses);
            for (int c=0; c<numClasses; c++) {
                branches[i].add(c, asn.getClassDist()[c].getCounts().getCount(i));
            }
        }
        Split split = new Split(original, branches);
        return gain.measure(split);
    }

    @Override
    public Decision getDecision() {
        return new Decision() {
            @Override
            public int decide(Attribute attribute) {
                return attribute.getAttributeInfo().findValue((String) attribute.getValue());
            }

            @Override
            public int getNumBranches() {
                return asn.getClassDist()[0].getNumValues();
            }
        };
    }
}
