package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.trees.BinarySearchTree;

import java.util.NoSuchElementException;

/**
 * Implementierung eines Binary Search Tree (BST).
 *
 * @param <K> Der Typ des Schlüssels, der vergleichbar sein muss.
 * @param <E> Der Typ des gespeicherten Elements.
 */
public class BSTree<K extends Comparable<K>, E> implements BinarySearchTree<K, E> {

    private long internalPathLength = 0; // Interne Pfadlänge, die die Summe aller Pfade im Baum speichert.
    private BSTNode<K, E> head; // Wurzelknoten des Baums.
    private int size; // Anzahl der Knoten im Baum.

    // Innere Klasse, die die Knoten des BST repräsentiert.
    private static class BSTNode<NK extends Comparable<NK>, NE> {
        public NK key; // Der Schlüssel des Knotens.
        public NE element; // Das gespeicherte Element des Knotens.
        public BSTNode<NK, NE> left; // Referenz auf den linken Kindknoten.
        public BSTNode<NK, NE> right; // Referenz auf den rechten Kindknoten.

        // Konstruktor, um einen neuen Knoten mit Schlüssel und Element zu erstellen.
        public BSTNode(NK key, NE element) {
            this.key = key;
            this.element = element;
        }
    }

    /**
     * Fügt ein neues Schlüssel-Wert-Paar in den Baum ein.
     * Falls der Schlüssel bereits existiert, wird das zugehörige Element aktualisiert.
     *
     * @param key Der Schlüssel des neuen Knotens.
     * @param e   Das gespeicherte Element des neuen Knotens.
     */
    @Override
    public void insert(K key, E e) {
        head = insertRecursive(head, key, e, 0); // Startet die rekursive Einfügeoperation.
    }

    // Rekursive Hilfsmethode für das Einfügen.
    private BSTNode<K, E> insertRecursive(BSTNode<K, E> node, K key, E e, int depth) {
        if (node == null) { // Wenn der aktuelle Knoten null ist, wird ein neuer Knoten erstellt.
            size++; // Erhöht die Größe des Baums.
            internalPathLength += depth; // Aktualisiert die interne Pfadlänge.
            return new BSTNode<>(key, e); // Erstellt und gibt den neuen Knoten zurück.
        }

        int cmp = key.compareTo(node.key); // Vergleicht den Schlüssel mit dem aktuellen Knoten.
        if (cmp < 0) { // Neuer Schlüssel ist kleiner.
            node.left = insertRecursive(node.left, key, e, depth + 1);
        } else if (cmp > 0) { // Neuer Schlüssel ist größer.
            node.right = insertRecursive(node.right, key, e, depth + 1);
        } else { // Schlüssel existiert bereits.
            node.element = e; // Aktualisiert das Element.
        }

        return node; // Gibt den aktuellen Knoten zurück, um die Baumstruktur aufrechtzuerhalten.
    }

    /**
     * Entfernt einen Knoten basierend auf seinem Schlüssel.
     *
     * @param key Der Schlüssel des zu entfernenden Knotens.
     * @throws NoSuchElementException Falls der Schlüssel nicht gefunden wird.
     */
    @Override
    public void remove(K key) throws NoSuchElementException {
        int sizePre = size; // Speichert die vorherige Größe des Baums.
        head = removeRecursive(head, key); // Startet die rekursive Entfernen-Operation.

        if (size != sizePre - 1) { // Überprüft, ob die Größe korrekt angepasst wurde.
            throw new AssertionError("Element wurde nicht korrekt entfernt.");
        }
    }

    // Rekursive Hilfsmethode zum Entfernen eines Knotens.
    private BSTNode<K, E> removeRecursive(BSTNode<K, E> node, K key) {
        if (node == null) { // Wenn der Knoten nicht existiert.
            throw new NoSuchElementException("Key not found: " + key);
        }

        // Vergleicht den übergebenen Schlüssel (key) mit dem Schlüssel des aktuellen Knotens (node.key).
        int cmp = key.compareTo(node.key);
        if (cmp < 0) { // Wenn der übergebene Schlüssel kleiner ist als der Schlüssel des aktuellen Knotens:
            node.left = removeRecursive(node.left, key); // Gehe in den linken Teilbaum und rufe die Methode rekursiv auf.
        } else if (cmp > 0) { // Wenn der übergebene Schlüssel größer ist als der Schlüssel des aktuellen Knotens:
            node.right = removeRecursive(node.right, key); // Gehe in den rechten Teilbaum und rufe die Methode rekursiv auf.
        } else { // Wenn der übergebene Schlüssel gleich dem Schlüssel des aktuellen Knotens ist:
            size--;

            // Falls der linke Kindknoten fehlt, gibt den rechten Kindknoten zurück (ersetzt den aktuellen Knoten).
            if (node.left == null) return node.right;
            // Falls der rechte Kindknoten fehlt, gibt den linken Kindknoten zurück (ersetzt den aktuellen Knoten).
            if (node.right == null) return node.left;

            // Wenn beide Kinder vorhanden sind:
            // Finde den kleinsten Knoten im rechten Teilbaum (Nachfolger des aktuellen Knotens).
            BSTNode<K, E> successor = findMin(node.right);
            // Setze den Schlüssel des Nachfolgers als neuen Schlüssel des aktuellen Knotens.
            node.key = successor.key;
            // Setze das Element des Nachfolgers als neues Element des aktuellen Knotens.
            node.element = successor.element;
            // Entferne den Nachfolger aus dem rechten Teilbaum, da er verschoben wurde.
            node.right = removeRecursive(node.right, successor.key);
        }


        return node; // Gibt den aktualisierten Knoten zurück.
    }

    // Hilfsmethode zum Finden des kleinsten Knotens im Teilbaum.
    private BSTNode<K, E> findMin(BSTNode<K, E> node) {
        while (node.left != null) { // Gehe immer weiter nach links.
            node = node.left;
        }
        return node; // Gibt den kleinsten Knoten zurück.
    }

    /**
     * Sucht ein Element basierend auf seinem Schlüssel.
     *
     * @param key Der Schlüssel des gesuchten Elements.
     * @return Das gespeicherte Element.
     * @throws NoSuchElementException Falls der Schlüssel nicht gefunden wird.
     */
    @Override
    public E get(K key) throws NoSuchElementException {
        BSTNode<K, E> node = getNode(key); // Sucht den Knoten.
        if (node == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }
        return node.element; // Gibt das gespeicherte Element zurück.
    }

    // Hilfsmethode, die den Knoten mit dem angegebenen Schlüssel findet.
    private BSTNode<K, E> getNode(K key) {
        BSTNode<K, E> current = head;
        while (current != null) { // Durchläuft den Baum iterativ.
            // Vergleicht den gesuchten Schlüssel (`key`) mit dem Schlüssel des aktuellen Knotens
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                // Wenn der gesuchte Schlüssel kleiner ist als der Schlüssel des aktuellen Knotens:
                current = current.left;
                // Bewege den Fokus auf den linken Kindknoten, weil der gesuchte Schlüssel nur im linken Teilbaum liegen kann.
            } else if (cmp > 0) {
                // Wenn der gesuchte Schlüssel größer ist als der Schlüssel des aktuellen Knotens:
                current = current.right;
                // Bewege den Fokus auf den rechten Kindknoten, weil der gesuchte Schlüssel nur im rechten Teilbaum liegen kann.
            } else {
                // Wenn der gesuchte Schlüssel gleich dem Schlüssel des aktuellen Knotens ist:
                return current;
                // Schlüssel gefunden: Gib den aktuellen Knoten zurück.
            }

        }
        return null; // Schlüssel nicht gefunden.
    }

    @Override
    public int size() {
        return size; // Gibt die Anzahl der Knoten im Baum zurück.
    }

    @Override
    public boolean contains(K key) {
        return getNode(key) != null; // Prüft, ob der Schlüssel existiert.
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Binary Search Tree:\n");
        toStringR(sb, head, 0); // Rekursive Darstellung des Baums.
        return sb.toString();
    }

    // Rekursive Methode zur Baumdarstellung.
    private void toStringR(StringBuffer sb, BSTNode<K, E> node, int depth) {
        if (node == null) {
            printNode(sb, null, depth); // Leerer Knoten.
            return;
        }

        toStringR(sb, node.right, depth + 1); // Rechter Teilbaum.
        printNode(sb, node.element, depth); // Aktueller Knoten.
        toStringR(sb, node.left, depth + 1); // Linker Teilbaum.
    }

    // Hilfsmethode zur Darstellung eines Knotens.
    private void printNode(StringBuffer sb, E item, int depth) {
        sb.append("  ".repeat(Math.max(0, depth))); // Einrückung entsprechend der Tiefe.
        sb.append("[");
        sb.append(item == null ? "{null}" : item.toString());
        sb.append("]\n");
    }

    /**
     * Berechnet die durchschnittliche Pfadlänge pro Knoten.
     *
     * @return Durchschnittliche Pfadlänge.
     */
    public double getAveragePathLength() {
        if (size == 0) return 0; // Kein Knoten im Baum.
        return (double) internalPathLength / size + 1;
    }
}
