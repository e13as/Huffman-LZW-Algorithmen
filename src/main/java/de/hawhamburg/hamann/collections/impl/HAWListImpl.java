package de.hawhamburg.hamann.collections.impl;

import de.hawhamburg.hamann.collections.HAWList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Implementierung der generischen Schnittstelle HAWList<E>.
 * Diese Klasse verwendet eine interne ArrayList, um die Elemente zu speichern,
 * und stellt grundlegende Listenoperationen wie Hinzufügen, Sortieren, Suchen und Iterieren bereit.
 *
 * @param <E> Der Typ der Elemente, die in der Liste gespeichert werden.
 */
public class HAWListImpl<E> implements HAWList<E> {

    // Interne Liste zur Speicherung der Elemente
    private List<E> elements;

    /**
     * Konstruktor, der eine leere Liste initialisiert.
     */
    public HAWListImpl() {
        this.elements = new ArrayList<>();
    }

    /**
     * Gibt die Anzahl der in der Liste gespeicherten Elemente zurück.
     *
     * @return Die aktuelle Größe der Liste.
     */
    @Override
    public int getSize() {
        return elements.size();
    }

    /**
     * Fügt ein neues Element ans Ende der Liste hinzu.
     *
     * @param item Das Element, das hinzugefügt werden soll.
     */
    @Override
    public void add(E item) {
        elements.add(item);
    }

    /**
     * Sortiert die Elemente in der Liste anhand des übergebenen Vergleichsobjekts (Comparator).
     *
     * @param o Der Comparator, der die Reihenfolge der Elemente definiert.
     */
    @Override
    public void sort(Comparator<E> o) {
        elements.sort(o);
    }

    /**
     * Sucht nach einem bestimmten Element in der Liste und gibt dessen Index zurück.
     * Falls das Element nicht vorhanden ist, wird -1 zurückgegeben.
     *
     * @param item Das Element, das gesucht werden soll.
     * @return Der Index des Elements in der Liste oder -1, falls das Element nicht gefunden wurde.
     */
    @Override
    public int find(E item) {
        return elements.indexOf(item);
    }

    /**
     * Gibt einen Iterator zurück, der es ermöglicht, über die Elemente der Liste zu iterieren.
     *
     * @return Ein Iterator über die Elemente der Liste.
     */
    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    /**
     * Gibt das Element an der angegebenen Position in der Liste zurück.
     *
     * @param index Der Index des gewünschten Elements.
     * @return Das Element an der angegebenen Position.
     * @throws IndexOutOfBoundsException Wenn der Index außerhalb des Bereichs liegt.
     */
    public E get(int index) {
        return elements.get(index);
    }
}
