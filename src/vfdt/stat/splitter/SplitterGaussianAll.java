package vfdt.stat.splitter;

import vfdt.measure.Counts;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.Split;
import vfdt.stat.attstat.AttStatGaussian;
import vfdt.stat.dist.DistributionGaussian;
import vfdt.tree.Decision;
import vfdt.tree.DecisionNumeric;

import java.util.ArrayList;
import java.util.List;

/**
 * Uses both bins and intersections as candidates.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class SplitterGaussianAll implements Splitter {
    private final AttStatGaussian asn;
    private final Gain            gain;
    private final int             numBins;
    private       Double          bestSplitValue;
    private       Double          bestG;

    public SplitterGaussianAll(AttStatGaussian asn, Gain gain, int numBins) {
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
        List<Double> points = getExactValues();
        bestG = Double.NEGATIVE_INFINITY;
        for (Double splitValue : points)
            if (splitValue != null)
                bestG = checkValue(original, splitValue);
        List<Double> binPoints = getBinValues();
        for (Double splitValue : binPoints)
            if (splitValue != null)
                bestG = checkValue(original, splitValue);
        return bestG;
    }

    private List<Double> getBinValues() {
        List<Double> points = new ArrayList<>();
        Double       step   = (asn.getMaxValue() - asn.getMinValue()) / (numBins + 1);
        for (int i = 1; i <= numBins; i++)
            points.add(asn.getMinValue() + i * step);
        return points;
    }

    private List<Double> getExactValues() {
        List<Double> points     = new ArrayList<>();
        int          numClasses = asn.getClassDist().length;
        for (int i = 0; i < numClasses; i++) {
            DistributionGaussian d1 = asn.getClassDist()[i];
            if (d1.getNumData() < 1)
                continue;
            for (int j = i + 1; j < numClasses; j++) {
                DistributionGaussian d2 = asn.getClassDist()[j];
                if (d2.getNumData() < 1)
                    continue;
                Double[] X = DistributionGaussian.intersect(d1, d2);
                for (Double x : X) {
                    if (x != null && x > asn.getMinValue() && x < asn.getMaxValue())
                        points.add(x);
                }
            }
        }
        return points;
    }

    private Double checkValue(Counts original, Double splitValue) throws Exception {
        Double g     = calculateGain(original, splitValue);
//        System.out.println(String.format("(%f, %f)", splitValue, g));
        if (g < 0) g = 0.;
        if (g > bestG) {
            bestG = g;
            bestSplitValue = splitValue;
        }
        return bestG;
    }

    private Double calculateGain(Counts original, Double splitValue) throws Exception {
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
        return g;
    }

    @Override
    public Decision getDecision() {
        return new DecisionNumeric(bestSplitValue);
    }
}
