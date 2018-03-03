package test.tree;

import org.junit.Test;
import vfdt.tree.Node;
import vfdt.tree.NodeBase;

public class TestNodeBase {
    @Test
    public void testBuild() {
        Node root = new NodeBase();
        Node left = new NodeBase().parent(root);
        Node right = new NodeBase().parent(root);
        root.setChildren(left, right);
    }
}
