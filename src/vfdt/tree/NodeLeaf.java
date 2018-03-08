package vfdt.tree;

import vfdt.data.Instance;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class NodeLeaf extends NodeBase {
    public abstract Integer classify(Instance instance);

    @Override
    public Node children(Node... children) {
        return this;
    }

    @Override
    public void setChildren(Node... children) {
    }
}
