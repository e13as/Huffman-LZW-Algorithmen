package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.trees.BinarySearchTree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class BSTree<K extends Comparable<K>,E> implements BinarySearchTree<K,E> {

    private long internalPathLength = 0;

    private static class BSTNode<NK extends Comparable<NK>, NE> {
        public NK key;
        public NE element;
        public BSTNode<NK, NE> left;
        public BSTNode<NK, NE> right;

        public BSTNode(NK key, NE e) {
            this.key = key;
            this.element = e;
        }
    }

    private BSTNode<K, E> head;

    private int size;

    @Override
    public void insert(K key, E e) {
        head = insertRecursive(head, key, e, 0);
    }

    @Override
    public void remove(K key) throws NoSuchElementException {
        head = removeRecursive(head, key);
    }

    private BSTNode<K, E> removeRecursive(BSTNode<K, E> node, K key) {
        if (node == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, key);
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, key);
        } else {
            // Knoten gefunden
            size--;
            if (node.left == null) return node.right; // Nur rechter Teilbaum
            if (node.right == null) return node.left; // Nur linker Teilbaum

            // Zwei Kinder: Ersetze mit Nachfolger
            BSTNode<K, E> successor = findMin(node.right);
            node.key = successor.key;
            node.element = successor.element;
            node.right = removeRecursive(node.right, successor.key);
        }

        return node;
    }

    private BSTNode<K, E> findMin(BSTNode<K, E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    @Override
    public E get(K key) throws NoSuchElementException {
        BSTNode<K, E> node = getNode(key);
        if (node == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }
        return node.element;
    }

    private BSTNode<K, E> getNode(K key) {
        BSTNode<K, E> current = head;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current; // Gefunden
            }
        }
        return null; // Nicht gefunden
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(K key) {
        return getNode(key) != null;
    }

    private BSTNode<K, E> insertRecursive(BSTNode<K, E> node, K key, E e, int depth) {
        if (node == null) {
            size++;
            internalPathLength += depth; // Hier wird die Pfadlänge erhöht
            return new BSTNode<>(key, e);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRecursive(node.left, key, e, depth + 1);
        } else if (cmp > 0) {
            node.right = insertRecursive(node.right, key, e, depth + 1);
        } else {
            node.element = e; // Schlüssel existiert, Wert wird aktualisiert
        }

        return node;
    }

    public double getAveragePathLength() {
        if (size == 0) return 0;
        return (double) internalPathLength / size + 1;
    }
}
