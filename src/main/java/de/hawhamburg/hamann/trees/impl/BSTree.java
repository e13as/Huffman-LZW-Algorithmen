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
        int sizePre = size;
        System.out.println("Größe vor dem Entfernen: " + sizePre);

        head = removeRecursive(head, key);

        System.out.println("Größe nach dem Entfernen: " + size);

        // Überprüfen, ob die Größe korrekt aktualisiert wurde
        if (size != sizePre - 1) {
            System.out.println("Fehler: Größe wurde nicht korrekt aktualisiert!");
        }
    }


    // Rekursive Hilfsmethode zum Entfernen eines Knotens.
    private BSTNode<K, E> removeRecursive(BSTNode<K, E> node, K key) {
        if (node == null) { // Wenn der Knoten nicht existiert.
            throw new NoSuchElementException("Key not found: " + key);
        }

        int cmp = key.compareTo(node.key);
// Vergleicht den zu löschenden Schlüssel (`key`) mit dem Schlüssel des aktuellen Knotens (`node.key`).

        if (cmp < 0) {
            // Wenn der zu löschende Schlüssel kleiner ist als der Schlüssel des aktuellen Knotens:
            node.left = removeRecursive(node.left, key);
            // Gehe in den linken Teilbaum und rufe die Methode rekursiv auf.
        } else if (cmp > 0) {
            // Wenn der zu löschende Schlüssel größer ist als der Schlüssel des aktuellen Knotens:
            node.right = removeRecursive(node.right, key);
            // Gehe in den rechten Teilbaum und rufe die Methode rekursiv auf.
        } else {
            // Der zu löschende Schlüssel wurde gefunden:
            size--;
            // Verringere die Größe des Baums um eins, da ein Knoten entfernt wird.
            System.out.println("Entferne Knoten: " + node.key);

            // Fall 1: Der Knoten hat kein linkes Kind.
            if (node.left == null) return node.right;
            // Gibt den rechten Kindknoten zurück, um den aktuellen Knoten zu ersetzen.

            // Fall 2: Der Knoten hat kein rechtes Kind.
            if (node.right == null) return node.left;
            // Gibt den linken Kindknoten zurück, um den aktuellen Knoten zu ersetzen.

            // Fall 3: Der Knoten hat zwei Kinder.
            BSTNode<K, E> successor = findMin(node.right);
            // Finde den kleinsten Knoten (Nachfolger) im rechten Teilbaum.

            System.out.println("Ersetze Knoten " + node.key + " mit Nachfolger " + successor.key);
            // Debug-Ausgabe: Zeigt an, welcher Knoten durch den Nachfolger ersetzt wird.

            node.key = successor.key;
            // Setze den Schlüssel des Nachfolgers als neuen Schlüssel des aktuellen Knotens.

            node.element = successor.element;
            // Setze das Element des Nachfolgers als neues Element des aktuellen Knotens.

            // Entferne den Nachfolger aus dem rechten Teilbaum, da er verschoben wurde.
            node.right = removeRecursive(node.right, successor.key);

            // Debugging: Überprüfe, ob der Nachfolger korrekt entfernt wurde.
            if (node.right != null && node.right.key.equals(successor.key)) {
                // Falls der Nachfolger noch vorhanden ist, wird dies protokolliert.
                System.out.println("Fehler: Nachfolger wurde nicht korrekt entfernt.");
            }
        }

        return node;

    }

    // Hilfsmethode zum Finden des kleinsten Knotens im Teilbaum.
    private BSTNode<K, E> findMin(BSTNode<K, E> node) {
        // Solange der aktuelle Knoten nicht null ist und einen linken Kindknoten hat, wird weiter nach links gegangen.
        while (node != null && node.left != null) {
            // Debug-Ausgabe: Gibt den Schlüssel des nächsten linken Kindes aus, zu dem gewechselt wird.
            System.out.println("Finde den kleinsten Knoten, gehe zu: " + node.left.key);
            // Verschiebe den Fokus auf den linken Kindknoten.
            node = node.left;
        }
        // Debug-Ausgabe: Gibt den kleinsten gefundenen Knoten aus (oder "null", wenn kein Knoten vorhanden ist).
        System.out.println("Kleinster Knoten: " + (node != null ? node.key : "null"));
        // Gibt den kleinsten Knoten zurück (dies ist der Knoten ganz links im Teilbaum).
        return node;
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
        if (head == null) {
            return "Der Baum ist leer.";
        }
        return buildPrettyTree(head, "", true);
    }

    private String buildPrettyTree(BSTNode<K, E> node, String prefix, boolean isLeft) {
        if (node == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append(prefix);
        sb.append(isLeft ? "├── " : "└── ");
        sb.append(node.key).append("\n");

        String childPrefix = prefix + (isLeft ? "│   " : "    ");
        if (node.left != null || node.right != null) {
            sb.append(buildPrettyTree(node.left, childPrefix, true));
            sb.append(buildPrettyTree(node.right, childPrefix, false));
        }

        return sb.toString();
    }

    private void printTree(BSTNode<K, E> node, int level) {
        if (node == null) {
            return;
        }

        // Gehe zuerst in den rechten Teilbaum
        printTree(node.right, level + 1);

        // Zeige den aktuellen Knoten mit Einrückung
        System.out.println("    ".repeat(level) + "-> " + node.key);

        // Gehe in den linken Teilbaum
        printTree(node.left, level + 1);
    }

    // Public-Methode für den Aufruf
    public void displayTree() {
        printTree(head, 0);
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
