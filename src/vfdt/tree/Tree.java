package vfdt.tree;

/**
 * Interface of a tree.
 *
 * @author Mehran Mirkhan
 * @version 1.0
 * @since 2018 Feb 28
 */
interface Tree {
    Node getRoot();

    int getHeight();

    void replaceNode(Node nodeOld, Node nodeNew) throws NoSuchFieldException;

    String print();
}
