package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.stat.SuffStatFactory;

import java.util.Collection;
import java.util.HashSet;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class VFDT extends DecisionTree {

    public VFDT(DatasetInfo datasetInfo, SplitPolicy splitPolicy, SuffStatFactory suffStatFactory) {
        super(datasetInfo, splitPolicy, suffStatFactory);
    }

    @Override
    public void learn(Instance instance, Attribute label) throws Exception {
        Node root = getRoot();
        NodeLeaf leaf = root.sortDown(instance);
        SplitInfo splitInfo = leaf.learn(instance, label);
        if (splitInfo != null) {
            ActiveLeaf activeLeaf = (ActiveLeaf) leaf;
            Collection<AttributeInfo> availableAtts = new HashSet<>(activeLeaf.getAvailableAttributes());
            availableAtts.remove(splitInfo.getAttributeInfo());
            int numBranches = splitInfo.getDecision().getNumBranches();
            DecisionNode decisionNode = new DecisionNode(splitInfo.getAttributeInfo(), splitInfo.getDecision());
            NodeLeaf[] children;
            if (leaf.getHeight() < splitPolicy.getMaxHeight()) {    // Leafs should be active
                children = new ActiveLeaf[numBranches];
                for (int i=0; i<numBranches; i++) {
                    children[i] = new ActiveLeaf(datasetInfo, splitPolicy, suffStatFactory, availableAtts);
                    children[i].setParent(decisionNode);
                    children[i].setHeight(leaf.getHeight() + 1);
                }
            } else {                // Leafs should not be active
                children = new NodeLeaf[numBranches];
                for (int i=0; i<numBranches; i++) {
                    children[i] = new NodeLeaf(datasetInfo);
                    children[i].setParent(decisionNode);
                    children[i].setHeight(leaf.getHeight() + 1);
                }
            }
            decisionNode.setHeight(leaf.getHeight());
            decisionNode.setChildren(children);
            leaf.replaceChild(leaf, decisionNode);
        }
    }

    @Override
    public String classify(Instance instance) {
        Node root = getRoot();
        NodeLeaf leaf = root.sortDown(instance);
        return leaf.classify(instance);
    }
}
