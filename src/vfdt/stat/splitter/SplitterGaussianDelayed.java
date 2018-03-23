package vfdt.stat.splitter;

import vfdt.measure.Counts;
import vfdt.measure.gain.Gain;
import vfdt.measure.gain.Split;
import vfdt.stat.attstat.AttStatGaussian;
import vfdt.stat.dist.DistributionGaussian;
import vfdt.tree.Decision;
import vfdt.tree.DecisionNumeric;
import vfdt.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class SplitterGaussianDelayed implements Splitter {
    private final AttStatGaussian asn;
    private final Gain            gain;
    private final int             numBins;
    private       Double          bestSplitValue;
    private       Counts          original;
    private       Double          bestG;

    public SplitterGaussianDelayed(AttStatGaussian asn, Gain gain, int numBins) {
        this.asn = asn;
        this.gain = gain;
        this.numBins = numBins;
    }

    @Override
    public Double getSplitGain() throws Exception {
        int numClasses = asn.getClassDist().length;
        original = new Counts(numClasses);
        for (int c = 0; c < numClasses; c++) {
            original.add(c, (double) asn.getClassDist()[c].getNumData());
        }
        bestG = 0.;
        bestSplitValue = null;
        List<Double> points = getBinValues();
        for (Double splitValue : points)
            checkValue(original, splitValue, false);
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
            if (original.getCount(i) < 2)
                continue;
            DistributionGaussian d1 = asn.getClassDist()[i];
            for (int j = i + 1; j < numClasses; j++) {
                if (original.getCount(j) < 2)
                    continue;
                DistributionGaussian d2 = asn.getClassDist()[j];
                Double[] X = DistributionGaussian.intersect(d1, d2);
                for (Double x : X)
                    if (x != null && x > asn.getMinValue() && x < asn.getMaxValue())
                        points.add(x);
            }
        }
        return points;
    }

    private Double checkValue(Counts original, Double splitValue, boolean debug) throws Exception {
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
        if (debug && Logger.isDebug()) {
            Logger.append("~~~~ split on value " + splitValue + " ~~~~\n");
            Logger.append("b1: " + branches[0].toString() + "\n");
            Logger.append("b2: " + branches[1].toString() + "\n");
            Logger.append("g: " + g + "\n");
            Logger.append("~~~~~~~~\n");
        }
        if (g > bestG) {
            bestG = g;
            bestSplitValue = splitValue;
        }
        return bestG;
    }

    @Override
    public Decision getDecision() {
        if (Logger.isDebug()) {
            Logger.append("numData: " + original.sum() + "\n");
            Logger.append("Original: " + original.toString() + "\n");
            Logger.append("AttStat:\n" + asn.toString());
            Logger.append("Bins: ");
            for (Double splitValue : getBinValues())
                Logger.append(Logger.df.format(splitValue) + ", ");
            Logger.append("\n");
            Logger.append("Best split value: " + bestSplitValue + "\n");
            Logger.append("Best gain: " + bestG + "\n");
            Logger.append("Exact: ");
        }
        List<Double> points = getExactValues();
        for (Double splitValue : points)
            try {
                checkValue(original, splitValue, true);
            } catch (Exception e) {
                System.out.println("Exception catched in SplitterGaussianDelayed.");
                e.printStackTrace();
            }
        if (Logger.isDebug()) {
            Logger.append("Best split value: " + bestSplitValue + "\n");
            Logger.append("Best gain: " + bestG + "\n\n");
        }
        return new DecisionNumeric(bestSplitValue);
    }
}
