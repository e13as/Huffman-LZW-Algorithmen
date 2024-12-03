package de.hawhamburg.hamann.trees.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class GraphTest {
    public static void main(String[] args) {
        // Beispielgraph aus der Grafik
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
        graph.addNode("F");
        graph.addNode("G");

        // Kanten hinzufügen (basierend auf der Grafik)
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

    private static void runBasicTests(Graph<String, Integer> graph) {
        System.out.println("Breitensuche (BFS): " + graph.breadthFirstSearch("A"));
        System.out.println("Tiefensuche (DFS): " + graph.depthFirstSearch("A"));

        // JVM Warm-up
        for (int i = 0; i < 10; i++) {
            graph.dijkstra("A");
        }

        // Laufzeit für Dijkstra messen (Mikrosekunden, mit Mittelwertbildung)
        int repetitions = 10;
        long totalDurationNs = 0;

        for (int i = 0; i < repetitions; i++) {
            long startTime = System.nanoTime();
            graph.dijkstra("A");
            long endTime = System.nanoTime();
            totalDurationNs += (endTime - startTime);
        }

        long averageDurationUs = totalDurationNs / repetitions / 1_000; // ns in µs umrechnen
        System.out.printf("Durchschnittliche Laufzeit für Dijkstra (kleiner Graph): %d µs\n", averageDurationUs);
    }

    private static void runPerformanceTest() {
        Random random = new Random();
        List<Integer> graphSizes = new ArrayList<>();
        List<Long> runtimes = new ArrayList<>();

        for (int n = 100; n <= 9600; n += 500) {
            Graph<Integer, Integer> testGraph = new Graph<>();

            // Knoten hinzufügen
            for (int i = 1; i <= n; i++) {
                testGraph.addNode(i);
            }

            // Kanten hinzufügen (zufällig, konsistente Dichte)
            for (int i = 0; i < n * 10; i++) {
                int from = random.nextInt(n) + 1;
                int to = random.nextInt(n) + 1;
                int weight = random.nextInt(100) + 1;
                if (from != to) {
                    testGraph.addEdge(from, to, weight);
                }
            }

            // JVM Warm-up
            for (int i = 0; i < 10; i++) {
                testGraph.dijkstra(1);
            }

            // Laufzeit messen (Mikrosekunden)
            long totalDurationNs = 0;
            int repetitions = (n <= 1000) ? 20 : 10;

            for (int i = 0; i < repetitions; i++) {
                long startTime = System.nanoTime();
                testGraph.dijkstra(1);
                long endTime = System.nanoTime();
                totalDurationNs += (endTime - startTime);
            }

            // Durchschnittliche Laufzeit in Mikrosekunden berechnen
            long averageDurationUs = totalDurationNs / repetitions / 1_000;

            // Ergebnisse speichern
            graphSizes.add(n);
            runtimes.add(averageDurationUs);

            System.out.printf("Graph size: %d, Avg Runtime: %d µs\n", n, averageDurationUs);
        }

        exportToCSV(graphSizes, runtimes);
    }

    private static void exportToCSV(List<Integer> graphSizes, List<Long> runtimes) {
        try (PrintWriter writer = new PrintWriter(new File("dijkstra_performance.csv"))) {
            writer.println("GraphSize,Runtime_µs");
            for (int i = 0; i < graphSizes.size(); i++) {
                writer.printf("%d,%d\n", graphSizes.get(i), runtimes.get(i));
            }
            System.out.println("Performance-Daten exportiert: dijkstra_performance.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
