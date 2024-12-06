package de.hawhamburg.hamann.trees.impl; // Deklaration des Pakets, in dem die Testklasse liegt

import de.hawhamburg.hamann.trees.BinaryTree; // Import der BinaryTree-Schnittstelle
import org.junit.Test; // Import der Test-Annotation von JUnit

import static org.junit.Assert.*; // Import von JUnit-Assertions für Testüberprüfungen

/**
 * Testklasse für die Implementierung eines binären Baums (BinaryTreeImpl).
 * Überprüft grundlegende Funktionalitäten wie Knotenprüfung, Getter- und Setter-Methoden sowie Traversierung.
 */
public class BinaryTreeImplTest {

    /**
     * Testet die Methode isLeaf(), die überprüft, ob ein Knoten ein Blatt ist.
     */
    @Test
    public void testIsLeaf() {
        // Erstellt einen neuen Knoten mit dem Wert 10
        BinaryTree<Integer> node = new BinaryTreeImpl<>(10);

        // Überprüft, ob der Knoten ein Blatt ist (keine Kinderknoten)
        assertTrue(node.isLeaf());

        // Fügt einen linken Kindknoten hinzu
        node.setLeftNode(new BinaryTreeImpl<>(5));

        // Überprüft, dass der Knoten kein Blatt mehr ist
        assertFalse(node.isLeaf());
    }

    /**
     * Testet die Getter- und Setter-Methoden für Daten und Kindknoten.
     */
    @Test
    public void testSetAndGet() {
        // Erstellt einen neuen Knoten mit dem Wert "Root"
        BinaryTree<String> node = new BinaryTreeImpl<>("Root");

        // Setzt den linken und rechten Kindknoten
        node.setLeftNode(new BinaryTreeImpl<>("Left"));
        node.setRightNode(new BinaryTreeImpl<>("Right"));

        // Überprüft, ob die Daten des Wurzelknotens korrekt sind
        assertEquals("Root", node.getData());

        // Überprüft, ob die Daten des linken Kindknotens korrekt sind
        assertEquals("Left", node.getLeftNode().getData());

        // Überprüft, ob die Daten des rechten Kindknotens korrekt sind
        assertEquals("Right", node.getRightNode().getData());
    }

    /**
     * Testet die Präorder-Traversierung (Pre-Order Traversal) des Baums.
     * Reihenfolge: Wurzel -> Linker Teilbaum -> Rechter Teilbaum.
     */
    @Test
    public void testPreOrderTraversal() {
        // Erstellt den Wurzelknoten
        BinaryTree<String> root = new BinaryTreeImpl<>("Root");

        // Setzt den linken und rechten Kindknoten
        root.setLeftNode(new BinaryTreeImpl<>("Left"));
        root.setRightNode(new BinaryTreeImpl<>("Right"));

        // StringBuilder, um die Traversierungsreihenfolge zu speichern
        StringBuilder result = new StringBuilder();

        // Führt die Präorder-Traversierung durch und sammelt die Ergebnisse
        root.visitPreOrder(node -> result.append(node.getData()).append(" "));

        // Überprüft die Traversierungsreihenfolge
        assertEquals("Root Left Right ", result.toString());
    }

    /**
     * Testet die Inorder-Traversierung (In-Order Traversal) des Baums.
     * Reihenfolge: Linker Teilbaum -> Wurzel -> Rechter Teilbaum.
     */
    @Test
    public void testInOrderTraversal() {
        // Erstellt den Wurzelknoten
        BinaryTree<String> root = new BinaryTreeImpl<>("Root");

        // Setzt den linken und rechten Kindknoten
        root.setLeftNode(new BinaryTreeImpl<>("Left"));
        root.setRightNode(new BinaryTreeImpl<>("Right"));

        // StringBuilder, um die Traversierungsreihenfolge zu speichern
        StringBuilder result = new StringBuilder();

        // Führt die Inorder-Traversierung durch und sammelt die Ergebnisse
        root.visitInOrder(node -> result.append(node.getData()).append(" "));

        // Überprüft die Traversierungsreihenfolge
        assertEquals("Left Root Right ", result.toString());
    }

    /**
     * Testet die Postorder-Traversierung (Post-Order Traversal) des Baums.
     * Reihenfolge: Linker Teilbaum -> Rechter Teilbaum -> Wurzel.
     */
    @Test
    public void testPostOrderTraversal() {
        // Erstellt den Wurzelknoten
        BinaryTree<String> root = new BinaryTreeImpl<>("Root");

        // Setzt den linken und rechten Kindknoten
        root.setLeftNode(new BinaryTreeImpl<>("Left"));
        root.setRightNode(new BinaryTreeImpl<>("Right"));

        // StringBuilder, um die Traversierungsreihenfolge zu speichern
        StringBuilder result = new StringBuilder();

        // Führt die Postorder-Traversierung durch und sammelt die Ergebnisse
        root.visitPostOrder(node -> result.append(node.getData()).append(" "));

        // Überprüft die Traversierungsreihenfolge
        assertEquals("Left Right Root ", result.toString());
    }
}
