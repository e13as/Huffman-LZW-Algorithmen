package de.hawhamburg.hamann.trees;

import java.util.function.Consumer;

/**
 * Interface für einen Binärbaum.
 *
 */
public interface BinaryTree<Data> {

    /**
     * Getter für das gespeicherte Element in
     * diesem Knoten.
     * @return Das in diesem Knoten gespeicherte Element
     */
    Data getData();

    /**
     * Setter für das in diesem Node gespeicherte
     * Element.
     * @param data Das zu speichernde Element
     */
    void setData(Data data);

    /**
     * Der linke Teilbaum dieses Baumes.
     * @return Der linke <code>BinaryTree</code> dieses Knotens
     */
    BinaryTree<Data> getLeftNode();

    /**
     * Setzt den linken Teilbaum dieses Baumes.
     *
     * @param tree Der neue linke Teilbaum
     */
    void setLeftNode(BinaryTree<Data> tree);

    /**
     * Der rechte Teilbaum dieses Baumes.
     * @return Der rechte <code>BinaryTree</code> dieses Knotens
     */
    BinaryTree<Data> getRightNode();

    /**
     * Setzt den rechten Teilbaum dieses Baumes.
     *
     * @param tree Der neue rechte Teilbaum
     */
    void setRightNode(BinaryTree<Data> tree);

    /**
     * <code>true</code>, sollte dieser Knoten ein Blatt sein, d. h. keine children haben
     * @return <code>true</code> falls ein Blatt, <code>false</code> sonst
     */
    boolean isLeaf();

    /**
     * Durchläuft den Baum in preorder und führt pro Knoten den Consumer aus.
     *
     * @param visitor Der <code>Consumer</code>, der für die einzelnen Knoten ausgeführt wird.
     */
    void visitPreOrder(Consumer<BinaryTree<Data>> visitor);

    /**
     * Durchläuft den Baum in inorder und führt pro Knoten den Consumer aus.
     *
     * @param visitor Der <code>Consumer</code>, der für die einzelnen Knoten ausgeführt wird.
     */
    void visitInOrder(Consumer<BinaryTree<Data>> visitor);

    /**
     * Durchläuft den Baum in postorder und führt pro Knoten den Consumer aus.
     *
     * @param visitor Der <code>Consumer</code>, der für die einzelnen Knoten ausgeführt wird.
     */
    void visitPostOrder(Consumer<BinaryTree<Data>> visitor);
}
