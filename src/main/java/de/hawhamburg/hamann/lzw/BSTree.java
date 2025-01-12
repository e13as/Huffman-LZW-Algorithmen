package de.hawhamburg.hamann.lzw;

import java.util.*;

public class BSTree {
    char data;
    int number;
    Map<Character, BSTree> subtries;

    public BSTree() {
        this.subtries = new HashMap<>();
    }

    public BSTree(char data, int number) {
        this();
        this.data = data;
        this.number = number;
    }

    public int getNumber() {
        return number;
    }



    public void insert(List<Character> prefix, int number) {
        BSTree current = this;
        for (char l : prefix) {
            current = current.subtries.computeIfAbsent(l, c -> new BSTree(c, number));
        }
    }

    public boolean includes(List<Character> prefix) {
        return this.getByPrefix(prefix) != null;
    }

    public List<Character> getByNum(int number) {
        return getByNum(number, new ArrayList<>());
    }

    public List<Character> getByNum(int number, List<Character> prefix) {
        if (this.number == number) return List.of();

        for (BSTree subBSTree : subtries.values()) {
            if (subBSTree.containsNumber(number)) {
                prefix.add(subBSTree.data);
                subBSTree.getByNum(number, prefix);
                break;
            }
        }
        return prefix;
    }

    public boolean containsNumber(int number) {
        if (this.number == number) return true;
        for (BSTree subBSTree : subtries.values()) {
            if (subBSTree.containsNumber(number)) return true;
        }
        return false;
    }

    public BSTree getByPrefix(List<Character> prefix) {
        Queue<Character> pfxQueue = new LinkedList<>(prefix);
        return getByPrefix(pfxQueue);
    }

    public BSTree getByPrefix(Queue<Character> prefix) {
        if (prefix.isEmpty()) return this;

        char cStart = prefix.remove();
        if (!this.subtries.containsKey(cStart)) return null;
        return this.subtries.get(cStart)
                .getByPrefix(prefix);
    }
}
