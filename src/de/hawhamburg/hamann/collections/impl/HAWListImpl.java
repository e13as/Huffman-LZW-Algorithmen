package de.hawhamburg.hamann.collections.impl;

import de.hawhamburg.hamann.collections.HAWList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class HAWListImpl<E> implements HAWList<E> {
    private List<E> elements;

    public HAWListImpl() {
        this.elements = new ArrayList<>();
    }

    @Override
    public int getSize() {
        return elements.size();
    }

    @Override
    public void add(E item) {
        elements.add(item);
    }

    @Override
    public void sort(Comparator<E> o) {
        elements.sort(o);
    }

    @Override
    public int find(E item) {
        return elements.indexOf(item);
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }
}
