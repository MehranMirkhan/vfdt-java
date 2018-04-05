package vfdt.tree;

/**
 * Basic implementation of Node.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
public abstract class NodeBase implements Node {
    private Node[]  children = null;
    private Node    parent   = null;
    private Integer height   = null;

    public Node parent(Node parent) {
        this.parent = parent;
        return this;
    }

    public Node children(Node... children) {
        this.children = children;
        return this;
    }

    public Node height(Integer height) {
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

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public boolean isLeaf() {
        return this.children == null || this.children.length == 0;
    }

    @Override
    public int getSubHeight() {
        if (isLeaf())
            return 1;
        int maxHeight = 0;
        for (Node child : getChildren()) {
            int height = child.getSubHeight();
            if (height > maxHeight)
                maxHeight = height;
        }
        return 1 + maxHeight;
    }

    @Override
    public int getNumSubNodes() {
        int numSubNodes = 1;
        if (!isLeaf())
            for (Node child : getChildren())
                numSubNodes += child.getNumSubNodes();
        return numSubNodes;
    }

    @Override
    public Integer getNumLeaves() {
        if (isLeaf())
            return 1;
        else {
            Integer numLeafs = 0;
            for (Node child : getChildren())
                numLeafs += child.getNumLeaves();
            return numLeafs;
        }
    }

    @Override
    public void replaceChild(Node nodeOld, Node nodeNew) throws NoSuchFieldException {
        Node[] children = getChildren();
        if (children == null)
            throw new NullPointerException("This node has no children.");
        else {
            boolean success = false;
            for (int i = 0; i < children.length; i++) {
                if (children[i] == nodeOld) {
                    children[i] = nodeNew;
                    success = true;
                    break;
                }
            }
            if (!success) {
                throw new NoSuchFieldException("Child not found");
            }
        }
    }

    @Override
    public void makeLeavesInActive() {
        for (int i=0; i<children.length; i++) {
            Node child = children[i];
            if (child instanceof ActiveLeaf) {
                NodeLeaf leaf = new NodeLeaf(((ActiveLeaf) child).getDatasetInfo());
                leaf.setClassCounts(((ActiveLeaf) child).getClassCounts());
                children[i] = leaf;
            } else if (!child.isLeaf()) {
                child.makeLeavesInActive();
            }
        }
    }
}

