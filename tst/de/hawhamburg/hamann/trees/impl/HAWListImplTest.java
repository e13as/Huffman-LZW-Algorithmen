package de.hawhamburg.hamann.trees.impl; // Paketdeklaration, um die Klasse logisch zu organisieren

import de.hawhamburg.hamann.collections.HAWList; // Import der HAWList-Schnittstelle
import de.hawhamburg.hamann.collections.impl.HAWListImpl; // Import der Implementierung von HAWList
import org.junit.Test; // Import der JUnit-Test-Annotation

import java.util.Comparator; // Import des Comparators für Sortieroperationen

import static junit.framework.TestCase.assertEquals; // Import der Methode zur Überprüfung auf Gleichheit
import static org.junit.Assert.assertFalse; // Import der Methode zum Überprüfen, dass eine Bedingung falsch ist
import static org.junit.Assert.assertTrue; // Import der Methode zum Überprüfen, dass eine Bedingung wahr ist

/**
 * Testklasse für die Implementierung der HAWList (HAWListImpl).
 * Überprüft grundlegende Operationen wie Hinzufügen, Finden, Sortieren und Überprüfung auf Vorhandensein eines Elements.
 */
public class HAWListImplTest {

    /**
     * Testet die Methode `add()` und überprüft, ob die Größe der Liste korrekt aktualisiert wird.
     */
    @Test
    public void testAddAndSize() {
        // Erstellt eine neue Instanz von HAWListImpl
        HAWList<Integer> list = new HAWListImpl<>();

        // Fügt zwei Elemente zur Liste hinzu
        list.add(1);
        list.add(2);

        // Überprüft, ob die Größe der Liste 2 ist
        assertEquals(2, list.getSize());
    }

    /**
     * Testet die Methode `find()`, die den Index eines Elements in der Liste zurückgibt.
     * Gibt -1 zurück, wenn das Element nicht gefunden wird.
     */
    @Test
    public void testFind() {
        // Erstellt eine neue Instanz von HAWListImpl
        HAWList<String> list = new HAWListImpl<>();

        // Fügt zwei Elemente zur Liste hinzu
        list.add("A");
        list.add("B");

        // Überprüft, ob der Index von "A" korrekt ist (Index 0)
        assertEquals(0, list.find("A"));

        // Überprüft, ob der Rückgabewert für ein nicht vorhandenes Element korrekt ist (-1)
        assertEquals(-1, list.find("C"));
    }

    /**
     * Testet die Methode `sort()`, die die Liste mit einem angegebenen Comparator sortiert.
     * Überprüft, ob die Liste nach der Sortierung korrekt ist.
     */
    @Test
    public void testSort() {
        // Erstellt eine neue Instanz von HAWListImpl
        HAWList<Integer> list = new HAWListImpl<>();

        // Fügt drei unsortierte Elemente zur Liste hinzu
        list.add(3);
        list.add(1);
        list.add(2);

        // Sortiert die Liste in natürlicher Reihenfolge (aufsteigend)
        list.sort(Comparator.naturalOrder());

        // Überprüft, ob das Element 1 nach der Sortierung an der Position 0 ist
        assertEquals(0, list.find(1));
    }

    /**
     * Testet die Methode `contains()`, die überprüft, ob ein Element in der Liste vorhanden ist.
     */
    @Test
    public void testContains() {
        // Erstellt eine neue Instanz von HAWListImpl
        HAWList<String> list = new HAWListImpl<>();

        // Fügt ein Element zur Liste hinzu
        list.add("X");

        // Überprüft, ob das Element "X" in der Liste enthalten ist
        assertTrue(list.contains("X"));

        // Überprüft, ob ein nicht vorhandenes Element ("Y") nicht in der Liste enthalten ist
        assertFalse(list.contains("Y"));
    }
}
