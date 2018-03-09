package vfdt.stat;

import vfdt.data.AttributeInfo;
import vfdt.stat.attstat.AttStatFactory;
import vfdt.stat.splitter.SplitterFactory;

import java.util.Collection;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class SuffStatFactoryBase implements SuffStatFactory {
    protected AttStatFactory  attStatFactory;
    protected SplitterFactory splitterFactory;

    public SuffStatFactoryBase(AttStatFactory attStatFactory,
                               SplitterFactory splitterFactory) {
        this.attStatFactory = attStatFactory;
        this.splitterFactory = splitterFactory;
    }

    @Override
    public SuffStat create(Collection<AttributeInfo> availableAtts) throws Exception {
        return new SuffStatBase(availableAtts, attStatFactory, splitterFactory);
    }
}
