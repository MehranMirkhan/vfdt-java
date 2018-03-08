package vfdt.tree;

/**
 * Basic implementation of Node.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public class NodeBase implements Node {
    private Node[] children = null;
    private Node parent = null;
    private int height = -1;        // -1 means unknown. Root is 0.

    public Node parent(Node parent) {
        this.parent = parent;
        return this;
    }

    public Node children(Node... children) {
        this.children = children;
        return this;
    }

    public Node height(int height) {
        this.height = height;
        return this;
    }

    public Node[] getChildren() {
        return this.children;
    }

    public void setChildren(Node... children) {
        this.children = children;
    }

    @Override
    public Node getParent() {
        return this.parent;
    }

    @Override
    public void setParent(Node parent) {
        this.parent = parent;
    }

    @Override
    public Node getChild(int index) {
        if (this.children == null)
            return null;
        else
            return this.children[index];
    }

    @Override
    public void setChild(int index, Node newChild) {
        if (this.children != null)
            this.children[index] = newChild;
    }

    @Override
    public boolean isLeaf() {
        return this.children == null || this.children.length == 0;
    }

    @Override
    public int getHeight() {
        return this.height;
    }
}

