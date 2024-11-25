package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.trees.BinarySearchTree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BSTree<K extends Comparable<K>,E> implements BinarySearchTree<K,E> {

    private long internalPathLength = 0;

    private static class BSTNode<NK extends Comparable<NK>, NE> {
        public NK key;
        public NE element;
        public  BSTNode<NK,NE> left;
        public  BSTNode<NK,NE> right;

        public BSTNode(NK key, NE e) {
            this.key = key;
            this.element = e;
        }
    }

    private BSTNode<K,E> head;

    private int size;

    @Override
    public void insert(K key, E e) {    }

    @Override
    public void remove(K key) throws NoSuchElementException {
        int sizePre = size;


        assert sizePre == size + 1;
    }

    @Override
    public E get(K key) throws NoSuchElementException {
        BSTNode<K, E> found = getNode(key);

        if (found == null) {
            throw new NoSuchElementException();
        }

        return found.element;
    }

    private BSTNode<K,E> getNode(K key) {
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(K key) {
        return getNode(key) != null;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BinaryST");

        toStringR(sb, head, 0);

        return sb.toString();
    }

    private void toStringR(StringBuffer sb, BSTNode<K, E> node, int h) {
        if (node == null) {
            printNode(sb, null, h);
            return;
        }

        toStringR(sb, node.right, h+1);
        printNode(sb, node.element, h);
        toStringR(sb, node.left, h+1);
    }

    private void printNode(StringBuffer sb, E item, int h) {
        sb.append("  ".repeat(Math.max(0, h)));

        sb.append("[");
        sb.append(item == null ? "{null}" : item.toString());
        sb.append("]\n");
    }
}
