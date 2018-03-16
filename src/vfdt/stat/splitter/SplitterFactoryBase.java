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
    private final Gain gain;
    private final int  numCandidates;
    private final String NumericalMethod;

    public SplitterFactoryBase(Gain gain, int numCandidates, String NumericalMethod) {
        this.gain = gain;
        this.numCandidates = numCandidates;
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
                return new SplitterGaussian(attStatGaussian, gain, numCandidates);
            case "exact":
                return new SplitterGaussianExact(attStatGaussian, gain);
            case "delayed":
                return new SplitterGaussianDelayed(attStatGaussian, gain, numCandidates);
            default:
                return null;
        }
    }
}
