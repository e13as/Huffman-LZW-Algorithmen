package de.hawhamburg.hamann.trees.impl;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class BSTreeTest {

    @Test
    public void insert() {
        BSTree<String, String> bst = new BSTree<>();
        bst.insert("a", "Element 1");
        bst.insert("e", "Element 2");
        bst.insert("k", "Element 3");
        // Alle drei Elemente eingefügt
        assertTrue(bst.contains("a"));
        assertTrue(bst.contains("e"));
        assertTrue(bst.contains("k"));

        bst.insert("c", "Element 4");
        bst.insert("g", "Element 5");
        assertTrue(bst.contains("a"));
        assertTrue(bst.contains("e"));
        assertTrue(bst.contains("k"));
        assertTrue(bst.contains("c"));
        assertTrue(bst.contains("g"));
    }

    @Test
    public void remove() {
        BSTree<String, String> bst = new BSTree<>();
        bst.insert("a", "Element1");
        assertTrue(bst.contains("a"));
        bst.remove("a");
        assertFalse(bst.contains("a"));
        bst.insert("h", "Element1");
        bst.insert("a", "Element2");
        bst.insert("d", "Element3");
        bst.insert("k", "Element4");
        bst.insert("b", "Element5");
    }

    @Test
    public void get() {
    }

    @Test
    public void size() {
        BSTree<String, String> bst = new BSTree<>();
        assertEquals(0, bst.size());
        bst.insert("a", "Element1");
        assertEquals(1, bst.size());
        bst.insert("a", "Element2");
        assertEquals(1, bst.size());
        bst.insert("b", "Element3");
        bst.insert("c", "Element1");
        assertEquals(3, bst.size());

        bst.remove("a");
        assertEquals(2, bst.size());

        try {
            bst.remove("a");
            fail();
        } catch (NoSuchElementException ignored) {

        }
        assertEquals(2, bst.size());

        bst.remove("c");
        assertEquals(1, bst.size());

        bst.remove("b");
        assertEquals(0, bst.size());
    }
}