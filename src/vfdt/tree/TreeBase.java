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

    @Override
    public Node getRoot() {
        return this.root;
    }

    void setRoot(Node root) {
        this.root = root;
    }

    @Override
    public int getHeight() {
        return root.getSubHeight();
    }

    @Override
    public int getNumNodes() {
        return root.getNumSubNodes();
    }

    @Override
    public void replaceNode(Node nodeOld, Node nodeNew) throws NoSuchFieldException {
        Node parent = nodeOld.getParent();
        parent.replaceChild(nodeOld, nodeNew);
//        setHeight(parent.getHeight() + parent.getSubHeight() - 1);
    }
}
