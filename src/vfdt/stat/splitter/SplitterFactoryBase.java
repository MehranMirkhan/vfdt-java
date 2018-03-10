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

    public SplitterFactoryBase(Gain gain, int numCandidates) {
        this.gain = gain;
        this.numCandidates = numCandidates;
    }

    @Override
    public Splitter createNominal(AttStatNominal attStatNominal) {
        return new SplitterNominal(attStatNominal, gain);
    }

    @Override
    public Splitter createNumerical(AttStatGaussian attStatGaussian) {
        return new SplitterGaussian(attStatGaussian, gain, numCandidates);
    }
}
