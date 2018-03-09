package vfdt.tree;

/**
 * Basic implementation of Tree.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public abstract class TreeBase implements Tree {
    private Node root;
    private int height = 1;

    @Override
    public Node getRoot() {
        return this.root;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void replaceNode(Node nodeOld, Node nodeNew) throws NoSuchFieldException {
        Node parent = nodeOld.getParent();
        parent.replaceChild(nodeOld, nodeNew);
        setHeight(parent.getHeight() + parent.getSubHeight() - 1);
    }
}
