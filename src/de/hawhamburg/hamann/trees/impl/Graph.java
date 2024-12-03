package de.hawhamburg.hamann.trees.impl;

import java.util.*;

public class Graph<K, W extends Number> {
    private final Map<K, List<Edge<K, W>>> adjacencyList;

    public Graph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addNode(K node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(K from, K to, W weight) {
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            throw new IllegalArgumentException("Both nodes must exist before adding an edge.");
        }
        if (weight.doubleValue() < 0) {
            throw new IllegalArgumentException("Negative weights are not supported by Dijkstra's algorithm.");
        }
        adjacencyList.get(from).add(new Edge<>(to, weight));
    }

    public List<Edge<K, W>> getEdges(K node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public Set<K> getNodes() {
        return adjacencyList.keySet();
    }

    public List<K> depthFirstSearch(K start) {
        List<K> visited = new ArrayList<>();
        Set<K> visitedSet = new HashSet<>();
        dfsRecursive(start, visited, visitedSet);
        return visited;
    }

    private void dfsRecursive(K current, List<K> visited, Set<K> visitedSet) {
        if (visitedSet.contains(current)) return;

        visited.add(current);
        visitedSet.add(current);

        for (Edge<K, W> edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
            dfsRecursive(edge.target, visited, visitedSet);
        }
    }

    public List<K> breadthFirstSearch(K start) {
        List<K> visited = new ArrayList<>();
        Set<K> visitedSet = new HashSet<>();
        Queue<K> queue = new LinkedList<>();

        queue.add(start);
        visitedSet.add(start);

        while (!queue.isEmpty()) {
            K current = queue.poll();
            visited.add(current);

            for (Edge<K, W> edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                if (!visitedSet.contains(edge.target)) {
                    queue.add(edge.target);
                    visitedSet.add(edge.target);
                }
            }
        }

        return visited;
    }

    public Map<K, Double> dijkstra(K start) {
        Map<K, Double> distances = new HashMap<>();
        PriorityQueue<Pair<K>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(p -> p.distance));
        Set<K> visited = new HashSet<>();

        for (K node : adjacencyList.keySet()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        distances.put(start, 0.0);
        priorityQueue.add(new Pair<>(start, 0.0));

        while (!priorityQueue.isEmpty()) {
            Pair<K> current = priorityQueue.poll();

            if (!visited.add(current.node)) continue;

            for (Edge<K, W> edge : adjacencyList.getOrDefault(current.node, Collections.emptyList())) {
                double newDist = current.distance + edge.weight.doubleValue();
                if (newDist < distances.get(edge.target)) {
                    distances.put(edge.target, newDist);
                    priorityQueue.add(new Pair<>(edge.target, newDist));
                }
            }
        }

        return distances;
    }

    // Überarbeitete Methode zur Durchführung von Tests
    public static void runBasicTests(Graph<String, Integer> graph) {
        System.out.println("Breitensuche (BFS): " + graph.breadthFirstSearch("A"));
        System.out.println("Tiefensuche (DFS): " + graph.depthFirstSearch("A"));

        // Laufzeit für Dijkstra messen (Mikrosekunden, mit Mittelwertbildung)
        int repetitions = 10; // Mehrfachmessungen für präzisere Ergebnisse
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (K node : adjacencyList.keySet()) {
            sb.append(node).append(" -> ");
            for (Edge<K, W> edge : adjacencyList.get(node)) {
                sb.append(edge.target).append("(").append(edge.weight).append("), ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static class Edge<K, W> {
        public final K target;
        public final W weight;

        public Edge(K target, W weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    private static class Pair<K> {
        public final K node;
        public final double distance;

        public Pair(K node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }
}
