package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;
import vfdt.data.Instance;

/**
 * Non-leaf node that represents a decision.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 08
 */
public class DecisionNode extends NodeBase {
    private final AttributeInfo attributeInfo;
    private final Decision      decision;

    public DecisionNode(AttributeInfo attributeInfo, Decision decision) {
        this.attributeInfo = attributeInfo;
        this.decision = decision;
    }

    public DecisionNode() {
        // Used only for test.
        attributeInfo = null;
        decision = null;
    }

    public NodeLeaf sortDown(Instance instance) {
        Attribute attribute = instance.getAttribute(attributeInfo.getName());
        int       nodeIndex = decision.decide(attribute);
        return this.getChild(nodeIndex).sortDown(instance);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String[] decisions = decision.describe(attributeInfo);
        for (int branchCounter=0; branchCounter<getChildren().length; branchCounter++) {
            for (int i=0; i<getHeight()-1; i++)
                sb.append("|\t");
            sb.append(decisions[branchCounter]).append('\n');
            sb.append(getChild(branchCounter).toString());
        }
        return sb.toString();
    }
}
