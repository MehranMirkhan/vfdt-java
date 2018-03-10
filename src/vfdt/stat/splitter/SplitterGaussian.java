package vfdt.stat.splitter;

import vfdt.data.Attribute;
import vfdt.measure.Counts;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.Split;
import vfdt.stat.attstat.AttStatGaussian;
import vfdt.tree.Decision;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public class SplitterGaussian implements Splitter {
    private final AttStatGaussian asn;
    private final Gain            gain;
    private final int             numCandidates;
    private       Double          bestSplitValue;

    public SplitterGaussian(AttStatGaussian asn, Gain gain, int numCandidates) {
        this.asn = asn;
        this.gain = gain;
        this.numCandidates = numCandidates;
    }

    @Override
    public Double getSplitGain() throws Exception {
        int    numClasses = asn.getClassDist().length;
        Counts original   = new Counts(numClasses);
        for (int c = 0; c < numClasses; c++) {
            original.add(c, (double) asn.getClassDist()[c].getNumData());
        }
        Double step  = (asn.getMaxValue() - asn.getMinValue()) / (numCandidates + 1);
        Double bestG = Double.NEGATIVE_INFINITY;
        for (int i = 1; i <= numCandidates; i++) {
            Double splitValue = asn.getMinValue() + i * step;
            Counts[] branches = new Counts[2];
            branches[0] = new Counts(numClasses);
            branches[1] = new Counts(numClasses);
            for (int c = 0; c < numClasses; c++) {
                Double[] result = asn.getClassDist()[c].split(splitValue);
                branches[0].add(c, result[0]);
                branches[1].add(c, result[1]);
            }
            Split split = new Split(original, branches);
            Double g = gain.measure(split);
            if (g > bestG) {
                bestG = g;
                bestSplitValue = splitValue;
            }
        }
        return bestG;
    }

    @Override
    public Decision getDecision() {
        return new Decision() {
            @Override
            public int decide(Attribute attribute) {
                Double value = (Double) attribute.getValue();
                int    index;
                if (value > bestSplitValue)
                    index = 1;
                else
                    index = 0;
                return index;
            }

            @Override
            public int getNumBranches() {
                return 2;
            }
        };
    }
}
