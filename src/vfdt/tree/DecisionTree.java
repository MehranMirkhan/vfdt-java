package vfdt.tree;

import vfdt.data.Instance;
import vfdt.ml.Classifier;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class DecisionTree extends TreeBase implements Classifier {
    public abstract NodeLeaf sortDown(Instance instance);
}
