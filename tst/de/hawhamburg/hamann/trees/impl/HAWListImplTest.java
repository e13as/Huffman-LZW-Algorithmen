package de.hawhamburg.hamann.trees.impl;

import de.hawhamburg.hamann.collections.HAWList;
import de.hawhamburg.hamann.collections.impl.HAWListImpl;
import org.junit.Test;

import java.util.Comparator;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HAWListImplTest {

    @Test
    public void testAddAndSize() {
        HAWList<Integer> list = new HAWListImpl<>();
        list.add(1);
        list.add(2);
        assertEquals(2, list.getSize());
    }

    @Test
    public void testFind() {
        HAWList<String> list = new HAWListImpl<>();
        list.add("A");
        list.add("B");
        assertEquals(0, list.find("A"));
        assertEquals(-1, list.find("C"));
    }

    @Test
    public void testSort() {
        HAWList<Integer> list = new HAWListImpl<>();
        list.add(3);
        list.add(1);
        list.add(2);
        list.sort(Comparator.naturalOrder());
        assertEquals(0, list.find(1)); // Nach Sortierung ist 1 an Position 0
    }

    @Test
    public void testContains() {
        HAWList<String> list = new HAWListImpl<>();
        list.add("X");
        assertTrue(list.contains("X"));
        assertFalse(list.contains("Y"));
    }
}


