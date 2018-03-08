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
public abstract class AttStatFactory {
    protected DatasetInfo datasetInfo;

    public AttStatFactory(DatasetInfo datasetInfo) {
        this.datasetInfo = datasetInfo;
    }

    public AttStat create(AttributeInfo attributeInfo) throws Exception {
        switch (attributeInfo.getType()) {
            case NOMINAL:
                return createNominal(attributeInfo);
            case NUMERICAL:
                return createNumerical(attributeInfo);
            default:
                throw new Exception("Unknown type");
        }
    }

    protected abstract AttStat createNominal(AttributeInfo attributeInfo);
    protected abstract AttStat createNumerical(AttributeInfo attributeInfo);
}
