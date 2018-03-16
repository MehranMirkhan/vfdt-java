package vfdt.stat.splitter;

import vfdt.measure.Counts;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.Split;
import vfdt.stat.attstat.AttStatGaussian;
import vfdt.tree.Decision;
import vfdt.tree.DecisionNumeric;

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
    private final int             numBins;
    private       Double          bestSplitValue;

    public SplitterGaussian(AttStatGaussian asn, Gain gain, int numBins) {
        this.asn = asn;
        this.gain = gain;
        this.numBins = numBins;
    }

    @Override
    public Double getSplitGain() throws Exception {
        int    numClasses = asn.getClassDist().length;
        Counts original   = new Counts(numClasses);
        for (int c = 0; c < numClasses; c++) {
            original.add(c, (double) asn.getClassDist()[c].getNumData());
        }
        Double step  = (asn.getMaxValue() - asn.getMinValue()) / (numBins + 1);
        Double bestG = Double.NEGATIVE_INFINITY;
        for (int i = 1; i <= numBins; i++) {
            Double splitValue = asn.getMinValue() + i * step;
            bestG = checkValue(original, bestG, splitValue);
        }
        return bestG;
    }

    private Double checkValue(Counts original, Double bestG, Double splitValue) throws Exception {
        int      numClasses = asn.getClassDist().length;
        Counts[] branches   = new Counts[2];
        branches[0] = new Counts(numClasses);
        branches[1] = new Counts(numClasses);
        for (int c = 0; c < numClasses; c++) {
            Double[] result = asn.getClassDist()[c].split(splitValue);
            if (result == null)
                continue;
            branches[0].add(c, result[0]);
            branches[1].add(c, result[1]);
        }
        Split  split = new Split(original, branches);
        Double g     = gain.measure(split);
        if (g > bestG) {
            bestG = g;
            bestSplitValue = splitValue;
        }
        return bestG;
    }

    @Override
    public Decision getDecision() {
        return new DecisionNumeric(bestSplitValue);
    }
}
