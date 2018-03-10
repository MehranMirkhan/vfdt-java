package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.stat.SuffStat;
import vfdt.stat.SuffStatFactory;

import java.util.Collection;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class ActiveLeaf extends NodeLeaf {
    private final SplitPolicy splitPolicy;
    private final SuffStat    suffStat;
    private int numData = 0;

    public ActiveLeaf(DatasetInfo datasetInfo,
                      SplitPolicy splitPolicy,
                      SuffStatFactory suffStatFactory,
                      Collection<AttributeInfo> availableAtts) throws Exception {
        super(datasetInfo);
        this.splitPolicy = splitPolicy;
        this.suffStat = suffStatFactory.create(availableAtts);
    }

    public Collection<AttributeInfo> getAvailableAttributes() {
        return suffStat.getAvailableAttributes();
    }

    @Override
    public SplitInfo learn(Instance instance, Attribute label) throws Exception {
        super.learn(instance, label);
        numData += 1;
        this.suffStat.update(instance, label);
        if (numData > splitPolicy.getGracePeriod()) {
            AttributeInfo attToSplit = suffStat.checkSplit(splitPolicy.getBound());
            if (attToSplit != null) {
                Decision decision = suffStat.getDecision();
                return new SplitInfo(attToSplit, decision);
            }
        }
        return null;
    }
}
