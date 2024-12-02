package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.trees.BinaryTree;

import java.util.function.Consumer;

public class BinaryTreeImpl<Data> implements BinaryTree<Data> {

    private Data data;
    private BinaryTree<Data> leftNode;
    private BinaryTree<Data> rightNode;

    public BinaryTreeImpl(Data data) {
        this.data = data;
        this.leftNode = null;
        this.rightNode = null;
    }

    @Override
    public Data getData() {
        return data;
    }

    @Override
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public BinaryTree<Data> getLeftNode() {
        return leftNode;
    }

    @Override
    public void setLeftNode(BinaryTree<Data> tree) {
        this.leftNode = tree;
    }

    @Override
    public BinaryTree<Data> getRightNode() {
        return rightNode;
    }

    @Override
    public void setRightNode(BinaryTree<Data> tree) {
        this.rightNode = tree;
    }

    @Override
    public boolean isLeaf() {
        return leftNode == null && rightNode == null;
    }

    @Override
    public void visitPreOrder(Consumer<BinaryTree<Data>> visitor) {
        visitor.accept(this); // Besuche den aktuellen Knoten
        if (leftNode != null) {
            leftNode.visitPreOrder(visitor);
        }
        if (rightNode != null) {
            rightNode.visitPreOrder(visitor);
        }
    }

    @Override
    public void visitInOrder(Consumer<BinaryTree<Data>> visitor) {
        if (leftNode != null) {
            leftNode.visitInOrder(visitor);
        }
        visitor.accept(this); // Besuche den aktuellen Knoten
        if (rightNode != null) {
            rightNode.visitInOrder(visitor);
        }
    }

    @Override
    public void visitPostOrder(Consumer<BinaryTree<Data>> visitor) {
        if (leftNode != null) {
            leftNode.visitPostOrder(visitor);
        }
        if (rightNode != null) {
            rightNode.visitPostOrder(visitor);
        }
        visitor.accept(this); // Besuche den aktuellen Knoten
    }
}
