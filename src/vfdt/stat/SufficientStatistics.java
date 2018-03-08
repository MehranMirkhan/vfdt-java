package vfdt.stat;

import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.stat.attstat.AttStat;
import vfdt.stat.attstat.AttStatFactory;
import vfdt.stat.splitter.Splitter;
import vfdt.tree.Decision;

import java.util.Collection;
import java.util.HashMap;

/**
 * The sufficient statistics stored in a node.
 * This class keeps track of how many data have arrived for each attribute and class.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class SufficientStatistics {
    private DatasetInfo                     datasetInfo;
    private HashMap<AttributeInfo, AttStat> attStats;
    private AttStatFactory                  factory;

    public SufficientStatistics(DatasetInfo datasetInfo,
                                Collection<AttributeInfo> availableAtts,
                                AttStatFactory factory) throws Exception {
        this.datasetInfo = datasetInfo;
        this.attStats = new HashMap<>();
        this.factory = factory;
        initAttStats(availableAtts);
    }

    protected void initAttStats(Collection<AttributeInfo> availableAtts) throws Exception {
        for (AttributeInfo attInfo : availableAtts) {
            attStats.put(attInfo, factory.create(attInfo));
        }
    }

    public void update(Instance instance) {
        // todo
    }

    public boolean checkSplit(Splitter splitter) {
        // todo
        return false;
    }

    public Decision getDecision() {
        // todo
        return null;
    }
}
