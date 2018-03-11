package vfdt.tree;

import vfdt.data.Attribute;
import vfdt.data.AttributeInfo;

/**
 * Represents a decision made in a node to sort down an instance.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 07
 */
public interface Decision {
    /**
     * Chooses a branch based on attribute value.
     *
     * @param attribute The attribute to decide on.
     * @return index of the chosen branch
     */
    int decide(Attribute attribute);

    int getNumBranches();

    public String[] describe(AttributeInfo attributeInfo);
}
