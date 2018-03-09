package vfdt.stat;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.Instance;
import vfdt.measure.bound.Bound;
import vfdt.tree.Decision;

import java.util.Collection;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public interface SuffStat {
    void update(Instance instance, Attribute label);

    AttributeInfo checkSplit(Bound bound) throws Exception;

    Decision getDecision();

    Collection<AttributeInfo> getAvailableAttributes();
}
