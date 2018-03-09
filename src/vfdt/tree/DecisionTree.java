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
    protected DatasetInfo datasetInfo;
    protected SplitPolicy splitPolicy;
    protected SuffStatFactory suffStatFactory;

    public DecisionTree(DatasetInfo datasetInfo,
                        SplitPolicy splitPolicy,
                        SuffStatFactory suffStatFactory) {
        this.datasetInfo = datasetInfo;
        this.splitPolicy = splitPolicy;
        this.suffStatFactory = suffStatFactory;
    }

    public NodeLeaf sortDown(Instance instance) {
        Node root = getRoot();
        if (root instanceof NodeLeaf)
            return (NodeLeaf) root;
        else
            return root.sortDown(instance);
    }
}
