package de.hawhamburg.hamann.trees.impl;

import java.util.*;

public class GraphTest {
    public static void main(String[] args) {
        // Beispielgraph aus der Vorlesung
        Graph<String, Integer> graph = createExampleGraph();

        // Laufzeitmessung für BFS
        long startTimeBFS = System.nanoTime();
        List<String> bfsPath = graph.bfsWithPath("A", "E");
        long endTimeBFS = System.nanoTime();
        long durationBFSUs = (endTimeBFS - startTimeBFS) / 1_000;
        System.out.printf("Breitensuche (BFS) Pfad von A nach E: %s, Laufzeit: %d µs\n", bfsPath, durationBFSUs);

        // Laufzeitmessung für DFS
        long startTimeDFS = System.nanoTime();
        List<String> dfsPath = graph.dfsWithPath("A", "E");
        long endTimeDFS = System.nanoTime();
        long durationDFSUs = (endTimeDFS - startTimeDFS) / 1_000;
        System.out.printf("Tiefensuche (DFS) Pfad von A nach E: %s, Laufzeit: %d µs\n", dfsPath, durationDFSUs);

        // Laufzeitmessung für Dijkstra
        long startTimeDijkstra = System.nanoTime();
        List<String> dijkstraPath = graph.dijkstraWithPath("A", "E");
        long endTimeDijkstra = System.nanoTime();
        long durationDijkstraUs = (endTimeDijkstra - startTimeDijkstra) / 1_000;
        System.out.printf("Dijkstra Pfad von A nach E: %s, Laufzeit: %d µs\n", dijkstraPath, durationDijkstraUs);
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
