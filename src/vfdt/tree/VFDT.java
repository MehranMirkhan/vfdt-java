package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.stat.SuffStatFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class VFDT extends DecisionTree {

    public VFDT(DatasetInfo datasetInfo, SplitPolicy splitPolicy, SuffStatFactory suffStatFactory) throws Exception {
        super(datasetInfo, splitPolicy, suffStatFactory);
        List<AttributeInfo> availableAtts = datasetInfo.getAttributeInfoAsList();
        availableAtts.remove(datasetInfo.getClassAttribute());
        this.setRoot(new ActiveLeaf(datasetInfo, splitPolicy, suffStatFactory, availableAtts).height(1));
    }

    @Override
    public void learn(Instance instance, Attribute label) throws Exception {
        Node      root      = getRoot();
        NodeLeaf  leaf      = root.sortDown(instance);
        SplitInfo splitInfo = leaf.learn(instance, label);
        if (splitInfo != null) {
            ActiveLeaf activeLeaf = (ActiveLeaf) leaf;
            Collection<AttributeInfo> availableAtts = new HashSet<>(activeLeaf.getAvailableAttributes());
            if (splitInfo.getAttributeInfo().isNominal())
                availableAtts.remove(splitInfo.getAttributeInfo());
            int numBranches = splitInfo.getDecision().getNumBranches();
            Node decisionNode = new DecisionNode(splitInfo.getAttributeInfo(), splitInfo.getDecision());
            Node[] children = new Node[numBranches];
            if (leaf.getHeight() < splitPolicy.getMaxHeight()) {    // Leafs should be active
                for (int i = 0; i < numBranches; i++) {
                    children[i] = new ActiveLeaf(datasetInfo, splitPolicy, suffStatFactory, availableAtts);
                    children[i].setParent(decisionNode);
                    children[i].setHeight(leaf.getHeight() + 1);
                }
            } else {                // Leafs should not be active
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

    @Override
    public String classify(Instance instance) {
        Node     root = getRoot();
        NodeLeaf leaf = root.sortDown(instance);
        return leaf.classify(instance);
    }

    @Override
    public String print() {
        return "---------- TREE ----------\n" +
               super.print() + getRoot().toString() +
               "--------------------------\n";
    }
}
