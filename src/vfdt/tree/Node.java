package vfdt.tree;

import vfdt.data.Instance;

/**
 * Interface of a node in a tree.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public interface Node {

    Node[] getChildren();

    void setChildren(Node... children);

    Node getParent();

    void setParent(Node parent);

    Node getChild(int index);

    void setChild(int index, Node newChild);

    Integer getHeight();

    void setHeight(Integer height);

    int getSubHeight();

    int getNumSubNodes();

    boolean isLeaf();

    void replaceChild(Node nodeOld, Node nodeNew) throws NoSuchFieldException;

    NodeLeaf sortDown(Instance instance);

    Integer getNumLeaves();

    void makeLeavesInActive();
}
