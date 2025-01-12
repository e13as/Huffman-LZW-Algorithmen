package de.hawhamburg.hamann.trees.impl;

public class Main {
    public static void main(String[] args) {
        BSTree<Integer, String> bst = new BSTree<>();

        // Elemente einfügen
        bst.insert(50, "Node 50");
        bst.insert(30, "Node 30");
        bst.insert(70, "Node 70");
        bst.insert(20, "Node 20");
        bst.insert(40, "Node 40");
        bst.insert(60, "Node 60");
        bst.insert(80, "Node 80");

        // Baumstruktur anzeigen
        System.out.println("Baumstruktur:");
        bst.displayTree();

        // Knoten entfernen
        System.out.println("\nEntferne Knoten 30...");
        bst.remove(30);

        // Baumstruktur nach dem Entfernen anzeigen
        System.out.println("\nBaumstruktur nach Entfernen von 30:");
        bst.displayTree();
    }
}
