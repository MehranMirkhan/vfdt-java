package vfdt.stat;

import vfdt.data.AttributeInfo;

import java.util.Collection;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public interface SuffStatFactory {
    SuffStat create(Collection<AttributeInfo> availableAtts) throws Exception;
}
