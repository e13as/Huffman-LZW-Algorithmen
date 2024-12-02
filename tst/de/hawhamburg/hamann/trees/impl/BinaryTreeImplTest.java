package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.trees.BinaryTree;
import org.junit.Test;

import static org.junit.Assert.*;

public class BinaryTreeImplTest {
    @Test
    public void testIsLeaf() {
        BinaryTree<Integer> node = new BinaryTreeImpl<>(10);
        assertTrue(node.isLeaf());

        node.setLeftNode(new BinaryTreeImpl<>(5));
        assertFalse(node.isLeaf());
    }

    @Test
    public void testSetAndGet() {
        BinaryTree<String> node = new BinaryTreeImpl<>("Root");
        node.setLeftNode(new BinaryTreeImpl<>("Left"));
        node.setRightNode(new BinaryTreeImpl<>("Right"));

        assertEquals("Root", node.getData());
        assertEquals("Left", node.getLeftNode().getData());
        assertEquals("Right", node.getRightNode().getData());
    }

    @Test
    public void testPreOrderTraversal() {
        BinaryTree<String> root = new BinaryTreeImpl<>("Root");
        root.setLeftNode(new BinaryTreeImpl<>("Left"));
        root.setRightNode(new BinaryTreeImpl<>("Right"));

        StringBuilder result = new StringBuilder();
        root.visitPreOrder(node -> result.append(node.getData()).append(" "));
        assertEquals("Root Left Right ", result.toString());
    }

    @Test
    public void testInOrderTraversal() {
        BinaryTree<String> root = new BinaryTreeImpl<>("Root");
        root.setLeftNode(new BinaryTreeImpl<>("Left"));
        root.setRightNode(new BinaryTreeImpl<>("Right"));

        StringBuilder result = new StringBuilder();
        root.visitInOrder(node -> result.append(node.getData()).append(" "));
        assertEquals("Left Root Right ", result.toString());
    }

    @Test
    public void testPostOrderTraversal() {
        BinaryTree<String> root = new BinaryTreeImpl<>("Root");
        root.setLeftNode(new BinaryTreeImpl<>("Left"));
        root.setRightNode(new BinaryTreeImpl<>("Right"));

        StringBuilder result = new StringBuilder();
        root.visitPostOrder(node -> result.append(node.getData()).append(" "));
        assertEquals("Left Right Root ", result.toString());
    }
}

