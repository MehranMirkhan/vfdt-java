package vfdt.tree;

/**
 * Basic implementation of Tree.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 04
 */
public class TreeBase implements Tree {
    protected Node root;
    protected int height = 0;

    public TreeBase() {
        this.root = new NodeBase();
    }

    @Override
    public Node getRoot() {
        return this.root;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}
