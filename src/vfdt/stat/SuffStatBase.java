package vfdt.stat;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.Instance;
import vfdt.measure.bound.Bound;
import vfdt.stat.attstat.AttStat;
import vfdt.stat.attstat.AttStatFactory;
import vfdt.stat.splitter.Splitter;
import vfdt.stat.splitter.SplitterFactory;
import vfdt.tree.Decision;
import vfdt.util.Logger;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The sufficient statistics stored in a node.
 * This class keeps track of how many data have arrived for each attribute and class.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class SuffStatBase implements SuffStat {
    private final LinkedHashMap<AttributeInfo, AttStat> attStats;
    private final LinkedHashMap<AttributeInfo, Splitter>      splitters;
    private final AttStatFactory                        attStatFactory;
    private final SplitterFactory                       splitterFactory;
    private       AttributeInfo                         attToSplit;
    private       int                                   numData;
    private final Collection<AttributeInfo>             availableAtts;

    public SuffStatBase(Collection<AttributeInfo> availableAtts,
                        AttStatFactory attStatFactory,
                        SplitterFactory splitterFactory) throws Exception {
        this.attStats = new LinkedHashMap<>();
        this.splitters = new LinkedHashMap<>();
        this.attStatFactory = attStatFactory;
        this.splitterFactory = splitterFactory;
        numData = 0;
        this.availableAtts = availableAtts;
        initAttStats(availableAtts);
    }

    private void initAttStats(Collection<AttributeInfo> availableAtts) throws Exception {
        for (AttributeInfo attInfo : availableAtts) {
            AttStat attStat = attStatFactory.create(attInfo);
            attStats.put(attInfo, attStat);
            splitters.put(attInfo, splitterFactory.create(attInfo, attStat));
        }
    }

    /**
     * Updates the statistics in this node.
     *
     * @param instance Data. Should not contain the label.
     * @param label    The label of data.
     */
    @Override
    public void update(Instance instance, Attribute label) {
        for (Attribute att : instance.getAtts()) {
            AttStat attStat = attStats.get(att.getAttributeInfo());
            if (attStat != null)
                attStat.update(att, label);
        }
        numData += 1;
    }

    public AttributeInfo checkSplit(Bound bound) throws Exception {
        LinkedHashMap<AttributeInfo, Double> gains = new LinkedHashMap<>(attStats.size());
        for (Map.Entry<AttributeInfo, Splitter> entry : splitters.entrySet()) {
            gains.put(entry.getKey(), entry.getValue().getSplitGain());
        }
        attToSplit = bound.isSplitNeeded(gains, numData);
        return attToSplit;
    }

    public Decision getDecision() {
        if (attToSplit == null)
            return null;
        else {
//            if (Logger.isDebug())
//                for (Map.Entry<AttributeInfo, Splitter> entry : splitters.entrySet()) {
//                    Logger.append(entry.getKey().getName() + ":\n");
//                    entry.getValue().getDecision();
//                }
            return splitters.get(attToSplit).getDecision();
        }
    }

    @Override
    public Collection<AttributeInfo> getAvailableAttributes() {
        return availableAtts;
    }
}
