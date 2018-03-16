package vfdt.stat.splitter;

import vfdt.measure.gain.Gain;
import vfdt.stat.attstat.AttStatGaussian;
import vfdt.stat.attstat.AttStatNominal;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public class SplitterFactoryBase extends SplitterFactory {
    private final Gain   gain;
    private final int    numBins;
    private final String NumericalMethod;

    public SplitterFactoryBase(Gain gain, int numBins, String NumericalMethod) {
        this.gain = gain;
        this.numBins = numBins;
        this.NumericalMethod = NumericalMethod;
    }

    @Override
    public Splitter createNominal(AttStatNominal attStatNominal) {
        return new SplitterNominal(attStatNominal, gain);
    }

    @Override
    public Splitter createNumerical(AttStatGaussian attStatGaussian) {
        switch (NumericalMethod) {
            case "bin":
                return new SplitterGaussian(attStatGaussian, gain, numBins);
            case "exact":
                return new SplitterGaussianExact(attStatGaussian, gain);
            case "delayed":
                return new SplitterGaussianDelayed(attStatGaussian, gain, numBins);
            default:
                return null;
        }
    }
}
