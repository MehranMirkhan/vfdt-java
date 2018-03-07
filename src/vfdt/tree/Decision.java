package vfdt.tree;

import vfdt.data.Attribute;

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
     * @param attribute
     * @return index of the chosen branch
     */
    public int decide(Attribute attribute);
    public int getNumBranches();
}
