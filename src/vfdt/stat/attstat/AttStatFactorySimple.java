package vfdt.stat.attstat;

import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public class AttStatFactorySimple extends AttStatFactory {
    public AttStatFactorySimple(DatasetInfo datasetInfo) {
        super(datasetInfo);
    }

    @Override
    protected AttStat createNominal(AttributeInfo attributeInfo) {
        return new AttStatNominal(datasetInfo.getNumClasses(), attributeInfo.getValues());
    }

    @Override
    protected AttStat createNumerical(AttributeInfo attributeInfo) {
        return new AttStatGaussian(datasetInfo.getNumClasses());
    }
}
