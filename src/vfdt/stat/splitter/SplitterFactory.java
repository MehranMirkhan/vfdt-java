package vfdt.stat.splitter;

import vfdt.data.AttributeInfo;
import vfdt.stat.attstat.AttStat;
import vfdt.stat.attstat.AttStatGaussian;
import vfdt.stat.attstat.AttStatNominal;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class SplitterFactory {
    public Splitter create(AttributeInfo attributeInfo, AttStat attStat) throws Exception {
        switch (attributeInfo.getType()) {
            case NOMINAL:
                return createNominal((AttStatNominal) attStat);
            case NUMERICAL:
                return createNumerical((AttStatGaussian) attStat);
            default:
                throw new Exception("Unknown type");
        }
    }

    protected abstract Splitter createNominal(AttStatNominal attStatNominal);

    protected abstract Splitter createNumerical(AttStatGaussian attStatGaussian);
}
