package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.trees.BinarySearchTree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementierung eines Binary Search Tree (BST), einer Datenstruktur, die geordnete Schlüssel-Wert-Paare speichert.
 * Unterstützt Operationen wie Einfügen, Entfernen und Suchen in logarithmischer Zeit im Durchschnitt.
 *
 * @param <K> Der Typ der Schlüssel, muss vergleichbar sein (Comparable).
 * @param <E> Der Typ der zugeordneten Werte.
 */
public class BSTree<K extends Comparable<K>, E> implements BinarySearchTree<K, E> {

    // Die gesamte interne Pfadlänge des Baumes (Summe der Pfadlängen zu allen Knoten).
    private long internalPathLength = 0;

    /**
     * Innere Klasse, die einen Knoten im BST darstellt.
     * Enthält den Schlüssel, das gespeicherte Element sowie Referenzen auf die linken und rechten Kinder.
     *
     * @param <NK> Der Typ der Schlüssel des Knotens.
     * @param <NE> Der Typ des gespeicherten Elements.
     */
    private static class BSTNode<NK extends Comparable<NK>, NE> {
        public NK key;                  // Schlüssel des Knotens
        public NE element;              // Gespeichertes Element
        public BSTNode<NK, NE> left;    // Linker Kindknoten
        public BSTNode<NK, NE> right;   // Rechter Kindknoten

        public BSTNode(NK key, NE e) {
            this.key = key;
            this.element = e;
        }
    }

    private BSTNode<K, E> head; // Wurzelknoten des Baums
    private int size;           // Anzahl der Knoten im Baum

    /**
     * Fügt ein Schlüssel-Wert-Paar in den Baum ein.
     * Falls der Schlüssel bereits existiert, wird der zugehörige Wert aktualisiert.
     *
     * @param key Der Schlüssel.
     * @param e   Das Element.
     */
    @Override
    public void insert(K key, E e) {
        head = insertRecursive(head, key, e, 0);
    }

    /**
     * Entfernt einen Knoten mit dem angegebenen Schlüssel aus dem Baum.
     *
     * @param key Der zu entfernende Schlüssel.
     * @throws NoSuchElementException Wenn der Schlüssel nicht im Baum gefunden wird.
     */
    @Override
    public void remove(K key) throws NoSuchElementException {
        head = removeRecursive(head, key);
    }

    // Rekursive Methode zum Entfernen eines Knotens.
    private BSTNode<K, E> removeRecursive(BSTNode<K, E> node, K key) {
        if (node == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRecursive(node.left, key);  // Gehe in den linken Teilbaum
        } else if (cmp > 0) {
            node.right = removeRecursive(node.right, key); // Gehe in den rechten Teilbaum
        } else {

            size--; // Reduziere die Größe des Baums

            if (node.left == null) return node.right; // Kein linker Teilbaum
            if (node.right == null) return node.left; // Kein rechter Teilbaum

            // Zwei Kinder: Ersetze mit dem kleinsten Knoten im rechten Teilbaum
            BSTNode<K, E> successor = findMin(node.right);
            node.key = successor.key;
            node.element = successor.element;
            node.right = removeRecursive(node.right, successor.key);
        }

        return node;
    }

    // Hilfsmethode zum Finden des kleinsten Knotens im Teilbaum.
    private BSTNode<K, E> findMin(BSTNode<K, E> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /**
     * Sucht und gibt das Element zurück, das dem angegebenen Schlüssel zugeordnet ist.
     *
     * @param key Der Schlüssel.
     * @return Das zugeordnete Element.
     * @throws NoSuchElementException Wenn der Schlüssel nicht gefunden wird.
     */
    @Override
    public E get(K key) throws NoSuchElementException {
        BSTNode<K, E> node = getNode(key);
        if (node == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }
        return node.element;
    }

    // Hilfsmethode zum Finden eines Knotens anhand des Schlüssels.
    private BSTNode<K, E> getNode(K key) {
        BSTNode<K, E> current = head;
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left; // Gehe in den linken Teilbaum
            } else if (cmp > 0) {
                current = current.right; // Gehe in den rechten Teilbaum
            } else {
                return current; // Schlüssel gefunden
            }
        }
        return null; // Schlüssel nicht gefunden
    }

    /**
     * Gibt die Anzahl der Knoten im Baum zurück.
     *
     * @return Die Anzahl der Knoten.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Überprüft, ob der Baum einen Knoten mit dem angegebenen Schlüssel enthält.
     *
     * @param key Der Schlüssel.
     * @return true, wenn der Schlüssel gefunden wurde, sonst false.
     */
    @Override
    public boolean contains(K key) {
        return getNode(key) != null;
    }

    // Rekursive Methode zum Einfügen eines Knotens.
    private BSTNode<K, E> insertRecursive(BSTNode<K, E> node, K key, E e, int depth) {
        if (node == null) {
            size++; // Erhöhe die Größe des Baums
            internalPathLength += depth; // Aktualisiere die Pfadlänge
            return new BSTNode<>(key, e);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRecursive(node.left, key, e, depth + 1);
        } else if (cmp > 0) {
            node.right = insertRecursive(node.right, key, e, depth + 1);
        } else {
            node.element = e; // Aktualisiere den Wert, wenn der Schlüssel bereits existiert
        }

        return node;
    }

    /**
     * Berechnet die durchschnittliche Pfadlänge aller Knoten im Baum.
     *
     * @return Die durchschnittliche Pfadlänge.
     */
    public double getAveragePathLength() {
        if (size == 0) return 0; // Verhindere Division durch Null
        return (double) internalPathLength / size + 1;
    }
}
