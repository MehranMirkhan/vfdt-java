package vfdt.tree;

/**
 * Interface of a node in a tree.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public interface Node {

    public Node[] getChildren();
    public void setChildren(Node... children);

    public Node getParent();
    public void setParent(Node parent);

    public Node getChild(int index);
    public void setChild(int index, Node newChild);

    public boolean isLeaf();
    public int getHeight();
}
