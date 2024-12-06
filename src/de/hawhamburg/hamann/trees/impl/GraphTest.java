package de.hawhamburg.hamann.trees.impl;

import java.util.*;

public class GraphTest {
    public static void main(String[] args) {
        // Beispielgraph aus der Vorlesung
        Graph<String, Integer> graph = createExampleGraph();

        // Tiefensuche und Breitensuche testen
        System.out.println("Breitensuche (BFS): " + graph.breadthFirstSearch("A"));
        System.out.println("Tiefensuche (DFS): " + graph.depthFirstSearch("A"));

        // Dijkstra-Algorithmus testen
        Map<String, Double> shortestPaths = graph.dijkstra("A");
        System.out.println("Kürzeste Pfade (Dijkstra) von A: " + shortestPaths);

        // Laufzeitüberprüfung für Dijkstra mit kleiner Testbasis
        long startTime = System.nanoTime();
        graph.dijkstra("A");
        long endTime = System.nanoTime();
        long durationUs = (endTime - startTime) / 1_000; // ns in µs umrechnen
        System.out.printf("Laufzeit für Dijkstra (Beispielgraph): %d µs\n", durationUs);
    }

    private static Graph<String, Integer> createExampleGraph() {
        Graph<String, Integer> graph = new Graph<>();

        // Knoten hinzufügen
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");
        graph.addNode("F");
        graph.addNode("G");

        // Kanten hinzufügen
        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "F", 10);
        graph.addEdge("A", "G", 5);
        graph.addEdge("B", "C", 7);
        graph.addEdge("B", "G", 2);
        graph.addEdge("C", "G", 1);
        graph.addEdge("C", "D", 12);
        graph.addEdge("G", "E", 8);
        graph.addEdge("G", "F", 4);
        graph.addEdge("D", "E", 4);
        graph.addEdge("F", "E", 3);

        return graph;
    }
}
