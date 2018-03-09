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

import java.util.Collection;
import java.util.HashMap;
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
    private HashMap<AttributeInfo, AttStat>  attStats;
    private HashMap<AttributeInfo, Splitter> splitters;
    private AttStatFactory                   attStatFactory;
    private SplitterFactory                  splitterFactory;
    private AttributeInfo                    attToSplit;
    private int                              numData;
    private Collection<AttributeInfo>        availableAtts;

    public SuffStatBase(Collection<AttributeInfo> availableAtts,
                        AttStatFactory attStatFactory,
                        SplitterFactory splitterFactory) throws Exception {
        this.attStats = new HashMap<>();
        this.splitters = new HashMap<>();
        this.attStatFactory = attStatFactory;
        this.splitterFactory = splitterFactory;
        numData = 0;
        this.availableAtts = availableAtts;
        initAttStats(availableAtts);
    }

    protected void initAttStats(Collection<AttributeInfo> availableAtts) throws Exception {
        for (AttributeInfo attInfo : availableAtts) {
            AttStat attStat = attStatFactory.create(attInfo);
            attStats.put(attInfo, attStat);
            splitters.put(attInfo, splitterFactory.create(attInfo, attStat));
        }
    }

    @Override
    public void update(Instance instance, Attribute label) {
        for (Attribute att : instance.getAtts()) {
            AttStat attStat = attStats.get(att.getAttributeInfo());
            attStat.update(att, label);
        }
        numData += 1;
    }

    public AttributeInfo checkSplit(Bound bound) throws Exception {
        HashMap<AttributeInfo, Double> gains = new HashMap<>(attStats.size());
        for (Map.Entry<AttributeInfo, Splitter> entry : splitters.entrySet()) {
            gains.put(entry.getKey(), entry.getValue().getSplitGain());
        }
        attToSplit = bound.isSplitNeeded(gains, numData);
        return attToSplit;
    }

    public Decision getDecision() {
        if (attToSplit == null)
            return null;
        else
            return splitters.get(attToSplit).getDecision();
    }

    @Override
    public Collection<AttributeInfo> getAvailableAttributes() {
        return availableAtts;
    }
}