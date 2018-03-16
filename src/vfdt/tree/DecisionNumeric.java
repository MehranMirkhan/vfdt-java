package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;

import java.text.DecimalFormat;

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
        DecimalFormat df = new DecimalFormat("0.0000");
        return new String[]{
                attributeInfo.getName() + " <= " + df.format(decisionValue),
                attributeInfo.getName() + " > " + df.format(decisionValue)};
    }
}
