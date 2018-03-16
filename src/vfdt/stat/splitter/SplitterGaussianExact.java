package vfdt.stat.splitter;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.measure.Counts;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.Split;
import vfdt.stat.attstat.AttStatGaussian;
import vfdt.stat.dist.DistributionGaussian;
import vfdt.tree.Decision;
import vfdt.tree.DecisionNumeric;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class SplitterGaussianExact implements Splitter {
    private final AttStatGaussian asn;
    private final Gain            gain;
    private       Double          bestSplitValue;
    private       Double          bestG;

    public SplitterGaussianExact(AttStatGaussian asn, Gain gain) {
        this.asn = asn;
        this.gain = gain;
    }

    @Override
    public Double getSplitGain() throws Exception {
        int    numClasses = asn.getClassDist().length;
        Counts original   = new Counts(numClasses);
        for (int c = 0; c < numClasses; c++) {
            original.add(c, (double) asn.getClassDist()[c].getNumData());
        }
        bestG = Double.NEGATIVE_INFINITY;
        for (int i=0; i<numClasses; i++) {
            if (original.getCount(i) < 2)
                continue;
            DistributionGaussian d1 = asn.getClassDist()[i];
            for (int j=i+1; j<numClasses; j++) {
                if (original.getCount(j) < 2)
                    continue;
                DistributionGaussian d2 = asn.getClassDist()[j];
                Double[] points = DistributionGaussian.intersect(d1, d2);
//                System.out.println("Candidates: " + points[0] + ", " + points[1]);
                for (Double splitValue : points)
                    if (splitValue != null && splitValue > asn.getMinValue() && splitValue < asn.getMaxValue())
                        bestG = checkValue(original, splitValue);
            }
        }
        return bestG;
    }

    private Double checkValue(Counts original, Double splitValue) throws Exception {
        int      numClasses = asn.getClassDist().length;
        Counts[] branches   = new Counts[2];
        branches[0] = new Counts(numClasses);
        branches[1] = new Counts(numClasses);
        for (int c = 0; c < numClasses; c++) {
            Double[] result = asn.getClassDist()[c].split(splitValue);
            if (result == null)
                continue;
//            System.out.println("split: " + result[0] + ", " + result[1]);
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
