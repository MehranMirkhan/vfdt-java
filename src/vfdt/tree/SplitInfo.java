package vfdt.tree;

import vfdt.data.AttributeInfo;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 09
 */
public class SplitInfo {
    private AttributeInfo attributeInfo;
    private Decision      decision;

    public SplitInfo(AttributeInfo attributeInfo, Decision decision) {
        this.attributeInfo = attributeInfo;
        this.decision = decision;
    }

    public AttributeInfo getAttributeInfo() {
        return attributeInfo;
    }

    public void setAttributeInfo(AttributeInfo attributeInfo) {
        this.attributeInfo = attributeInfo;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    @Override
    public String toString() {
        return "SplitInfo{" +
                "attribute=" + attributeInfo.getName() +
                ", numBranches=" + decision.getNumBranches() +
                '}';
    }
}
