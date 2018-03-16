package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 12
 */
public class DecisionNumeric implements Decision {

    public Double decisionValue;

    public DecisionNumeric(Double decisionValue) {
        this.decisionValue = decisionValue;
    }

    @Override
    public int decide(Attribute attribute) {
        Double value = (Double) attribute.getValue();
        int    index;
        if (value > decisionValue)
            index = 1;
        else
            index = 0;
        return index;
    }

    @Override
    public int getNumBranches() {
        return 2;
    }

    @Override
    public String[] describe(AttributeInfo attributeInfo) {
        return new String[]{
                attributeInfo.getName() + " <= " + decisionValue,
                attributeInfo.getName() + " > " + decisionValue};
    }
}
