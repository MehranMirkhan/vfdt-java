package vfdt.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.stat.SuffStat;
import vfdt.stat.SuffStatFactory;

import java.util.Arrays;
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
    private static final Logger logger = LogManager.getLogger();

    public ActiveLeaf(DatasetInfo datasetInfo,
                      SplitPolicy splitPolicy,
                      SuffStatFactory suffStatFactory,
                      Collection<AttributeInfo> availableAtts) throws Exception {
        super(datasetInfo);
        this.splitPolicy = splitPolicy;
        this.suffStat = suffStatFactory.create(availableAtts);
    }

    public ActiveLeaf() {
        // Used only for test.
        splitPolicy = null;
        suffStat = null;
    }

    public Collection<AttributeInfo> getAvailableAttributes() {
        return suffStat.getAvailableAttributes();
    }

    @Override
    public SplitInfo learn(Instance instance, Attribute label) throws Exception {
        super.learn(instance, label);
        numData += 1;
        this.suffStat.update(instance, label);
        boolean checkSplit = splitPolicy.getGracePeriod() == null ||
                numData % splitPolicy.getGracePeriod() == 0;
        if (checkSplit) {
            AttributeInfo attToSplit = suffStat.checkSplit(splitPolicy.getBound());
            if (attToSplit != null) {       // Split is required
                Decision decision = suffStat.getDecision();
                SplitInfo splitInfo = new SplitInfo(attToSplit, decision);
                logger.debug("Splitting. numData = {}. {}",
                        numData,
                        Arrays.toString(decision.describe(attToSplit)));
                return splitInfo;
            }
        }
        return null;
    }
}
