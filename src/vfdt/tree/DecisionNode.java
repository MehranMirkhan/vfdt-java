package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.Instance;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public abstract class DecisionNode extends NodeBase {
    protected Decision decision;

    public DecisionNode(Decision decision) {
        this.decision = decision;
    }

    public Node sortDown(Instance instance) {
        Attribute att = null;
        return null;
    }
}
