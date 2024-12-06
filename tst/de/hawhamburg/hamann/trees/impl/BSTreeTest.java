package de.hawhamburg.hamann.trees.impl;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class BSTreeTest {

    /**
     * Testet die Insert-Funktionalität des BSTree.
     * Es wird überprüft, ob Elemente korrekt eingefügt wurden und im Baum enthalten sind.
     */
    @Test
    public void insert() {
        BSTree<String, String> bst = new BSTree<>();

        // Elemente einfügen
        bst.insert("a", "Element 1");
        bst.insert("e", "Element 2");
        bst.insert("k", "Element 3");

        // Überprüfung: Alle eingefügten Elemente sollten enthalten sein
        assertTrue(bst.contains("a"));
        assertTrue(bst.contains("e"));
        assertTrue(bst.contains("k"));

        // Weitere Elemente einfügen
        bst.insert("c", "Element 4");
        bst.insert("g", "Element 5");

        // Überprüfung: Auch die neuen Elemente sollten enthalten sein
        assertTrue(bst.contains("a"));
        assertTrue(bst.contains("e"));
        assertTrue(bst.contains("k"));
        assertTrue(bst.contains("c"));
        assertTrue(bst.contains("g"));
    }

    /**
     * Testet die Remove-Funktionalität des BSTree.
     * Überprüft, ob Elemente korrekt entfernt werden und ob der Baum auf den Zustand reagiert.
     */
    @Test
    public void remove() {
        BSTree<String, String> bst = new BSTree<>();

        // Ein Element einfügen und entfernen
        bst.insert("a", "Element1");
        assertTrue(bst.contains("a"));
        bst.remove("a");
        assertFalse(bst.contains("a"));

        // Mehrere Elemente einfügen
        bst.insert("h", "Element1");
        bst.insert("a", "Element2");
        bst.insert("d", "Element3");
        bst.insert("k", "Element4");
        bst.insert("b", "Element5");
    }

    /**
     * Testet die Get-Funktionalität des BSTree.
     * Überprüft, ob Elemente korrekt abgerufen werden können und wie der Baum auf nicht vorhandene Schlüssel reagiert.
     */
    @Test
    public void get() {
        BSTree<String, String> bst = new BSTree<>();

        // Elemente einfügen
        bst.insert("a", "Element 1");
        bst.insert("b", "Element 2");
        bst.insert("c", "Element 3");

        // Überprüfung: Elemente sollten korrekt abgerufen werden können
        assertEquals("Element 1", bst.get("a"));
        assertEquals("Element 2", bst.get("b"));
        assertEquals("Element 3", bst.get("c"));

        // Ein Schlüssel überschreiben
        bst.insert("b", "New Element 2");
        assertEquals("New Element 2", bst.get("b")); // Der neue Wert sollte zurückgegeben werden

        // Abfrage eines nicht vorhandenen Schlüssels sollte eine Ausnahme werfen
        try {
            bst.get("d");
            fail("Expected NoSuchElementException");
        } catch (NoSuchElementException e) {
            // Ausnahme wurde korrekt geworfen
            assertEquals("Key not found: d", e.getMessage());
        }
    }

    /**
     * Testet die Size-Funktionalität des BSTree.
     * Überprüft, ob die Größe des Baums korrekt verwaltet wird, wenn Elemente eingefügt und entfernt werden.
     */
    @Test
    public void size() {
        BSTree<String, String> bst = new BSTree<>();

        // Überprüfung: Anfangs sollte die Größe 0 sein
        assertEquals(0, bst.size());

        // Ein Element einfügen
        bst.insert("a", "Element1");
        assertEquals(1, bst.size());

        // Gleichen Schlüssel erneut einfügen (Überschreiben sollte die Größe nicht ändern)
        bst.insert("a", "Element2");
        assertEquals(1, bst.size());

        // Weitere Elemente einfügen
        bst.insert("b", "Element3");
        bst.insert("c", "Element1");
        assertEquals(3, bst.size());

        // Ein Element entfernen
        bst.remove("a");
        assertEquals(2, bst.size());

        // Versuch, ein nicht vorhandenes Element zu entfernen, sollte eine Ausnahme werfen
        try {
            bst.remove("a");
            fail();
        } catch (NoSuchElementException ignored) {
            // Ausnahme wurde erwartet
        }
        assertEquals(2, bst.size());

        // Weitere Elemente entfernen
        bst.remove("c");
        assertEquals(1, bst.size());

        bst.remove("b");
        assertEquals(0, bst.size());
    }
}
