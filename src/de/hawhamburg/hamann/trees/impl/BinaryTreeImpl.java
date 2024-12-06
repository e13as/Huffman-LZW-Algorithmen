package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.trees.BinaryTree;
import java.util.function.Consumer;

/**
 * Implementierung eines Binärbaums, der das BinaryTree-Interface nutzt.
 * Diese Klasse repräsentiert einzelne Knoten des Baums und ermöglicht grundlegende Baumoperationen.
 */
public class BinaryTreeImpl<Data> implements BinaryTree<Data> {

    // Der Wert (Daten), der im aktuellen Knoten gespeichert ist.
    private Data data;

    // Referenz auf den linken Kindknoten.
    private BinaryTree<Data> leftNode;

    // Referenz auf den rechten Kindknoten.
    private BinaryTree<Data> rightNode;

    /**
     * Konstruktor, um einen neuen Knoten mit einem Wert zu erstellen.
     * @param data Die Daten, die im Knoten gespeichert werden sollen.
     */
    public BinaryTreeImpl(Data data) {
        this.data = data;        // Initialisiere die gespeicherten Daten.
        this.leftNode = null;    // Standardmäßig hat der Knoten keine Kinder.
        this.rightNode = null;
    }

    /**
     * Gibt die im aktuellen Knoten gespeicherten Daten zurück.
     * @return Die gespeicherten Daten.
     */
    @Override
    public Data getData() {
        return data;
    }

    /**
     * Setzt die im aktuellen Knoten gespeicherten Daten.
     * @param data Die neuen Daten, die gespeichert werden sollen.
     */
    @Override
    public void setData(Data data) {
        this.data = data;
    }

    /**
     * Gibt den linken Kindknoten des aktuellen Knotens zurück.
     * @return Der linke Kindknoten.
     */
    @Override
    public BinaryTree<Data> getLeftNode() {
        return leftNode;
    }

    /**
     * Setzt den linken Kindknoten des aktuellen Knotens.
     * @param tree Der neue linke Kindknoten.
     */
    @Override
    public void setLeftNode(BinaryTree<Data> tree) {
        this.leftNode = tree;
    }

    /**
     * Gibt den rechten Kindknoten des aktuellen Knotens zurück.
     * @return Der rechte Kindknoten.
     */
    @Override
    public BinaryTree<Data> getRightNode() {
        return rightNode;
    }

    /**
     * Setzt den rechten Kindknoten des aktuellen Knotens.
     * @param tree Der neue rechte Kindknoten.
     */
    @Override
    public void setRightNode(BinaryTree<Data> tree) {
        this.rightNode = tree;
    }

    /**
     * Überprüft, ob der aktuelle Knoten ein Blatt ist (keine Kinder hat).
     * @return true, wenn der Knoten keine Kinder hat, sonst false.
     */
    @Override
    public boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }

    /**
     * Durchläuft den Baum in PreOrder-Reihenfolge (Wurzel, Links, Rechts).
     * @param visitor Eine Funktion, die auf jeden Knoten angewendet wird.
     */
    @Override
    public void visitPreOrder(Consumer<BinaryTree<Data>> visitor) {
        visitor.accept(this); // Verarbeite den aktuellen Knoten.
        if (leftNode != null) {
            leftNode.visitPreOrder(visitor); // Besuche den linken Teilbaum.
        }
        if (rightNode != null) {
            rightNode.visitPreOrder(visitor); // Besuche den rechten Teilbaum.
        }
    }

    /**
     * Durchläuft den Baum in InOrder-Reihenfolge (Links, Wurzel, Rechts).
     * @param visitor Eine Funktion, die auf jeden Knoten angewendet wird.
     */
    @Override
    public void visitInOrder(Consumer<BinaryTree<Data>> visitor) {
        if (leftNode != null) {
            leftNode.visitInOrder(visitor); // Besuche den linken Teilbaum.
        }
        visitor.accept(this); // Verarbeite den aktuellen Knoten.
        if (rightNode != null) {
            rightNode.visitInOrder(visitor); // Besuche den rechten Teilbaum.
        }
    }

    /**
     * Durchläuft den Baum in PostOrder-Reihenfolge (Links, Rechts, Wurzel).
     * @param visitor Eine Funktion, die auf jeden Knoten angewendet wird.
     */
    @Override
    public void visitPostOrder(Consumer<BinaryTree<Data>> visitor) {
        if (leftNode != null) {
            leftNode.visitPostOrder(visitor); // Besuche den linken Teilbaum.
        }
        if (rightNode != null) {
            rightNode.visitPostOrder(visitor); // Besuche den rechten Teilbaum.
        }
        visitor.accept(this); // Verarbeite den aktuellen Knoten.
    }
}
