package test.tree;

import org.junit.Test;
import vfdt.tree.ActiveLeaf;
import vfdt.tree.DecisionNode;
import vfdt.tree.Node;

/**
 * %Description%
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Mar 11
 */
public class TestNodeBase {
    @Test
    public void testReplaceChild() throws Exception {
        Node root = new DecisionNode();
        Node leaf1 = new ActiveLeaf();
        Node leaf2 = new ActiveLeaf();
        Node[] children = new Node[2];
        children[0] = leaf1;
        children[1] = leaf2;
        root.setChildren(children);
        leaf1.setParent(root);
        leaf2.setParent(root);

        Node newDecision = new DecisionNode();
        leaf2.getParent().replaceChild(leaf2, newDecision);
    }
}
