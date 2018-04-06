package vfdt.tree;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.ml.StopCriterion;
import vfdt.stat.SuffStatFactory;

import java.util.*;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class VFDT extends DecisionTree {
    private final SplitPolicy     splitPolicy;
    private final SuffStatFactory suffStatFactory;
    private static final Logger logger = LogManager.getLogger();
    private boolean isLearningEnded = false;

    public VFDT(DatasetInfo datasetInfo, SplitPolicy splitPolicy, SuffStatFactory suffStatFactory) throws Exception {
        super(datasetInfo);
        this.splitPolicy = splitPolicy;
        this.suffStatFactory = suffStatFactory;
        List<AttributeInfo> availableAtts = datasetInfo.getAttributeInfoAsList();
        availableAtts.remove(datasetInfo.getClassAttribute());
        this.setRoot(new ActiveLeaf(datasetInfo, splitPolicy, suffStatFactory, availableAtts).height(1));
    }

    @Override
    public void learn(Instance instance, Attribute label, StopCriterion stopCriterion) throws Exception {
        if (!isLearningEnded && stopCriterion != null && stopCriterion.shouldStop(this)) {
            makeLeavesInActive();
            isLearningEnded = true;
        }
        Node      root      = getRoot();
        NodeLeaf  leaf      = root.sortDown(instance);
        SplitInfo splitInfo = leaf.learn(instance, label);
        if (splitInfo != null) {
            ActiveLeaf activeLeaf = (ActiveLeaf) leaf;
            Collection<AttributeInfo> availableAtts = new ArrayList<>(activeLeaf.getAvailableAttributes());
            if (splitInfo.getAttributeInfo().isNominal())
                availableAtts.remove(splitInfo.getAttributeInfo());
            int numBranches = splitInfo.getDecision().getNumBranches();
            Node decisionNode = new DecisionNode(splitInfo.getAttributeInfo(), splitInfo.getDecision());
            Node[] children = new Node[numBranches];
            if (leaf.getHeight() < splitPolicy.getMaxHeight() - 1) {    // Leafs should be active
                for (int i = 0; i < numBranches; i++) {
                    children[i] = new ActiveLeaf(datasetInfo, splitPolicy, suffStatFactory, availableAtts);
                    children[i].setParent(decisionNode);
                    children[i].setHeight(leaf.getHeight() + 1);
                }
            } else {                // Leafs should not be active
                logger.debug("Maximum height is reached. Split is done with inActive leaves.");
                for (int i = 0; i < numBranches; i++) {
                    children[i] = new NodeLeaf(datasetInfo);
                    children[i].setParent(decisionNode);
                    children[i].setHeight(leaf.getHeight() + 1);
                }
            }
            decisionNode.setHeight(leaf.getHeight());
            decisionNode.setChildren(children);
            if (leaf.getParent() != null) {
                decisionNode.setParent(leaf.getParent());
                leaf.getParent().replaceChild(leaf, decisionNode);
            }
            else
                this.setRoot(decisionNode);
        }
    }

    private void makeLeavesInActive() {
        Node      root      = getRoot();
        if (root instanceof ActiveLeaf) {
            NodeLeaf leaf = new NodeLeaf(((ActiveLeaf) root).getDatasetInfo());
            leaf.setClassCounts(((ActiveLeaf) root).getClassCounts());
            setRoot(leaf);
        } else {
            root.makeLeavesInActive();
        }
    }

    @Override
    public String classify(Instance instance) {
        NodeLeaf leaf = sortDown(instance);
        return leaf.classify(instance);
    }

    @Override
    public String print(boolean drawTree) {
        return "---------- TREE ----------\n" +
               super.print(drawTree) + (drawTree ? getRoot().toString() : "") +
               "--------------------------\n";
    }
}
