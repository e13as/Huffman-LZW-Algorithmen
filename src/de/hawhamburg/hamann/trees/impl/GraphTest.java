package de.hawhamburg.hamann.trees.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphTest {
    public static void main(String[] args) {
        // Beispielgraph aus den Folien
        Graph<String, Integer> graph = createExampleGraph();
        runBasicTests(graph);

        // Performanztest
        runPerformanceTest();
    }

    private static Graph<String, Integer> createExampleGraph() {
        Graph<String, Integer> graph = new Graph<>();

        // Knoten hinzufügen
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addNode("E");

        // Kanten hinzufügen
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 6);
        graph.addEdge("B", "D", 3);
        graph.addEdge("B", "C", 5);
        graph.addEdge("C", "D", 4);
        graph.addEdge("C", "E", 2);

        return graph;
    }

    private static void runBasicTests(Graph<String, Integer> graph) {
        System.out.println("Breitensuche (BFS): " + graph.breadthFirstSearch("A"));
        System.out.println("Tiefensuche (DFS): " + graph.depthFirstSearch("A"));
        System.out.println("Dijkstra von 'A': " + graph.dijkstra("A"));
    }

    private static void runPerformanceTest() {
        Random random = new Random();
        List<Integer> graphSizes = new ArrayList<>();
        List<Long> runtimes = new ArrayList<>();

        for (int n = 100; n <= 5000; n += 100) {
            Graph<Integer, Integer> graph = new Graph<>();

            // Knoten hinzufügen
            for (int i = 1; i <= n; i++) {
                graph.addNode(i);
            }

            // Kanten hinzufügen (zufällig)
            for (int i = 0; i < n * 10; i++) {
                int from = random.nextInt(n) + 1;
                int to = random.nextInt(n) + 1;
                int weight = random.nextInt(100) + 1; // Gewicht zwischen 1 und 100
                if (from != to) {
                    graph.addEdge(from, to, weight);
                }
            }

            // Laufzeit messen
            long startTime = System.nanoTime();
            graph.dijkstra(1); // Startknoten: 1
            long endTime = System.nanoTime();

            // Ergebnisse speichern
            graphSizes.add(n);
            runtimes.add(endTime - startTime);

            System.out.printf("Graph size: %d, Runtime: %d ns\n", n, endTime - startTime);
        }

        exportToCSV(graphSizes, runtimes);
    }

    private static void exportToCSV(List<Integer> graphSizes, List<Long> runtimes) {
        try (PrintWriter writer = new PrintWriter(new File("dijkstra_performance.csv"))) {
            writer.println("GraphSize,Runtime_ns");
            for (int i = 0; i < graphSizes.size(); i++) {
                writer.printf("%d,%d\n", graphSizes.get(i), runtimes.get(i));
            }
            System.out.println("Performance-Daten exportiert: dijkstra_performance.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

