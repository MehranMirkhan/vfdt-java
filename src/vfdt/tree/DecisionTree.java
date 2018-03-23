package vfdt.tree;

import vfdt.data.DatasetInfo;
import vfdt.data.Instance;
import vfdt.ml.Classifier;
import vfdt.stat.SuffStatFactory;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class DecisionTree extends TreeBase implements Classifier {
    final DatasetInfo     datasetInfo;
    final SplitPolicy     splitPolicy;
    final SuffStatFactory suffStatFactory;

    DecisionTree(DatasetInfo datasetInfo,
                 SplitPolicy splitPolicy,
                 SuffStatFactory suffStatFactory) {
        this.datasetInfo = datasetInfo;
        this.splitPolicy = splitPolicy;
        this.suffStatFactory = suffStatFactory;
    }

    public NodeLeaf sortDown(Instance instance) {
        Node root = getRoot();
        if (root.isLeaf())
            return (NodeLeaf) root;
        else
            return root.sortDown(instance);
    }

    public Integer getNumLeaves() {
        return getRoot().getNumLeaves();
    }

    public String print(boolean drawTree) {
        return "Height = " + getHeight() +
               "\nNumber of nodes = " + getNumNodes() +
               "\nNumber of leaves = " + getNumLeaves() + "\n";
    }
}
