package de.hawhamburg.hamann.trees.impl; // Paketdeklaration für die Organisation der Klasse

import java.util.*; // Import von Java-Datenstrukturen wie Map, List, Queue usw.

/**
 * Generische Klasse zur Darstellung eines gerichteten, gewichteten Graphen.
 * @param <K> Typ der Knoten (z. B. String, Integer).
 * @param <W> Typ der Gewichtungen, der von Number abgeleitet sein muss (z. B. Integer, Double).
 */
public class Graph<K, W extends Number> {
    // Adjazenzliste zur Speicherung der Knoten und deren ausgehenden Kanten
    private final Map<K, List<Edge<K, W>>> adjacencyList;

    /**
     * Konstruktor zur Initialisierung eines leeren Graphen.
     * Die Knoten und Kanten werden in einer HashMap gespeichert.
     */
    public Graph() {
        // Initialisiert die Adjazenzliste als HashMap
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Fügt einen neuen Knoten zum Graphen hinzu.
     * Falls der Knoten bereits existiert, wird nichts geändert.
     * @param node Der hinzuzufügende Knoten.
     */
    public void addNode(K node) {
        // Fügt den Knoten hinzu, falls er noch nicht in der Adjazenzliste existiert
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Fügt eine gerichtete Kante zwischen zwei existierenden Knoten hinzu.
     * Das Gewicht der Kante muss nicht negativ sein.
     * @param from Der Ausgangsknoten der Kante.
     * @param to Der Zielknoten der Kante.
     * @param weight Das Gewicht der Kante.
     * @throws IllegalArgumentException Wenn die Knoten nicht existieren oder das Gewicht negativ ist.
     */
    public void addEdge(K from, K to, W weight) {
        // Prüft, ob beide Knoten in der Adjazenzliste existieren
        if (!adjacencyList.containsKey(from) || !adjacencyList.containsKey(to)) {
            throw new IllegalArgumentException("Both nodes must exist before adding an edge.");
        }
        // Prüft, ob das Gewicht negativ ist
        if (weight.doubleValue() < 0) {
            throw new IllegalArgumentException("Negative weights are not supported by Dijkstra's algorithm.");
        }
        // Fügt eine Kante vom Ausgangsknoten zum Zielknoten mit dem angegebenen Gewicht hinzu
        adjacencyList.get(from).add(new Edge<>(to, weight));
    }


    /**
     * Führt eine Tiefensuche (DFS) ab einem Startknoten durch.
     * Die Methode verwendet eine rekursive Hilfsmethode.
     * @param start Der Startknoten der Suche.
     * @return Eine Liste der besuchten Knoten in der Reihenfolge ihres Besuchs.
     */
    public List<K> dfsWithPath(K start, K target) {
        // Map zur Speicherung des Vorgängers jedes Knotens
        Map<K, K> previous = new HashMap<>();
        // Set zur Verfolgung der bereits besuchten Knoten
        Set<K> visited = new HashSet<>();

        // Starte die rekursive Tiefensuche
        dfsRecursiveWithPath(start, target, visited, previous);

        // Rekonstruiere den Pfad vom Zielknoten zum Startknoten
        return reconstructPath(previous, start, target);
    }

    private boolean dfsRecursiveWithPath(K current, K target, Set<K> visited, Map<K, K> previous) {
        // Markiere den aktuellen Knoten als besucht
        visited.add(current);

        // Abbruchbedingung: Zielknoten gefunden
        if (current.equals(target)) {
            return true;
        }

        // Durchlaufe alle Kanten des aktuellen Knotens, um dessen Nachbarn zu besuchen
        for (Edge<K, W> edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
            K neighbor = edge.target; // Zielknoten der aktuellen Kante (Nachbar des aktuellen Knotens)

            // Überprüfe, ob der Nachbar bereits besucht wurde
            if (!visited.contains(neighbor)) {
                previous.put(neighbor, current); // Speichere den aktuellen Knoten als Vorgänger des Nachbarn

                // Führe eine rekursive Tiefensuche für den Nachbarn durch
                // Wenn der Zielknoten während der Rekursion gefunden wird, beende die Suche
                if (dfsRecursiveWithPath(neighbor, target, visited, previous)) {
                    return true; // Ziel wurde gefunden, Rekursion beenden
                }
            }
        }
        // kein Pfad gefunden wurde
        return false;
    }

        /**
         * Führt eine Breitensuche (BFS) ab einem Startknoten durch.
         * Diese Methode verwendet eine Warteschlange, um die Suche iterativ zu gestalten.
         * @param start Der Startknoten der Suche.
         * @return Eine Liste der besuchten Knoten in der Reihenfolge ihres Besuchs.
         */
    public List<K> bfsWithPath(K start, K target) {
        // Warteschlange für die Knoten, die besucht werden sollen
        Queue<K> queue = new LinkedList<>();
        // Map zur Speicherung des Vorgängers jedes Knotens
        Map<K, K> previous = new HashMap<>();
        // Set zur Verfolgung der bereits besuchten Knoten
        Set<K> visited = new HashSet<>();

        // Startknoten in die Warteschlange einfügen und als besucht markieren
        queue.add(start);
        visited.add(start);

        // BFS-Hauptschleife
        while (!queue.isEmpty()) {
            K current = queue.poll(); // Entfernt den ersten Knoten aus der Warteschlange

            // Abbruch, wenn der Zielknoten erreicht ist
            if (current.equals(target)) {
                break;
            }

            // Iteriere über alle Nachbarn des aktuellen Knotens
            for (Edge<K, W> edge : adjacencyList.getOrDefault(current, Collections.emptyList())) {
                K neighbor = edge.target;
                if (!visited.contains(neighbor)) { // Falls Nachbar noch nicht besucht wurde
                    visited.add(neighbor); // Als besucht markieren
                    previous.put(neighbor, current); // Vorgänger speichern
                    queue.add(neighbor); // Nachbar zur Warteschlange hinzufügen
                }
            }
        }

        // Rekonstruiere den Pfad vom Zielknoten zum Startknoten
        return reconstructPath(previous, start, target);
    }

    /**
     * Implementiert Dijkstras Algorithmus zur Berechnung der kürzesten Pfade vom Startknoten.
     * @param start Der Startknoten.
     * @return Eine Map, die jedem Knoten die kürzeste Distanz vom Startknoten zuordnet.
     */
    public List<K> dijkstraWithPath(K start, K target) {
        // eine Map zur Speicherung der kürzesten Distanzen .
        Map<K, Double> distances = new HashMap<>();
        // Speichert die vorherigen Knoten, um den kürzesten Pfad rekonstruieren zu können.
        Map<K, K> previous = new HashMap<>();
        // Prioritätswarteschlange für die Knoten, sortiert nach Distanz.
        PriorityQueue<Pair<K>> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(p -> p.distance));
        // Set zur Verfolgung der bereits besuchten Knoten.
        Set<K> visited = new HashSet<>();

        // Initialisiert alle Distanzen auf "unendlich".
        for (K node : adjacencyList.keySet()) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }
        // Setzt die Distanz des Startknotens auf 0.
        distances.put(start, 0.0);
        // Fügt den Startknoten mit Distanz 0 zur Warteschlange hinzu.
        priorityQueue.add(new Pair<>(start, 0.0));

        // Hauptschleife des Algorithmus
        while (!priorityQueue.isEmpty()) {
            // Entfernt den Knoten mit der kleinsten Distanz aus der Warteschlange.
            Pair<K> current = priorityQueue.poll();

            // Überspringt den Knoten, wenn er bereits besucht wurde.
            if (!visited.add(current.node)) continue;

            // Bricht ab, wenn der Zielknoten erreicht ist.
            if (current.node.equals(target)) {
                break; // Zielknoten gefunden
            }

            // Iteriert über alle Nachbarn des aktuellen Knotens.
            for (Edge<K, W> edge : adjacencyList.getOrDefault(current.node, Collections.emptyList())) {
                // Berechnet die neue Distanz zum Zielknoten.
                double newDist = current.distance + edge.weight.doubleValue();

                // Aktualisiert die Distanz, wenn die neue Distanz kürzer ist.
                if (newDist < distances.get(edge.target)) {
                    distances.put(edge.target, newDist); // Kürzeste Distanz speichern.
                    previous.put(edge.target, current.node); // Vorgänger speichern.
                    // Fügt den Zielknoten mit der aktualisierten Distanz zur Warteschlange hinzu.
                    priorityQueue.add(new Pair<>(edge.target, newDist));
                }
            }
        }

        // Rekonstruiert den Pfad vom Startknoten zum Zielknoten.
        return reconstructPath(previous, start, target);
    }

    private List<K> reconstructPath(Map<K, K> previous, K start, K target) {
        // Liste zur Speicherung des rekonstruierten Pfads.
        List<K> path = new LinkedList<>();

        // Beginnt beim Zielknoten und geht rückwärts zum Startknoten.
        for (K at = target; at != null; at = previous.get(at)) {
            path.add(0, at); // Fügt den aktuellen Knoten am Anfang der Liste ein.
        }

        // Prüft, ob der Pfad tatsächlich beim Startknoten beginnt.
        if (path.get(0).equals(start)) {
            return path; // Erfolgreich rekonstruierter Pfad.
        }

        return Collections.emptyList(); // Kein Pfad gefunden.
    }



    @Override
    public String toString() {
        // Initialisiert einen StringBuilder, um den Graphen als String darzustellen.
        StringBuilder sb = new StringBuilder();

        // Iteriert über alle Knoten des Graphen (Schlüssel der Adjazenzliste).
        for (K node : adjacencyList.keySet()) {
            // Fügt den aktuellen Knoten und seine ausgehenden Kanten in das String-Format ein.
            sb.append(node).append(" -> ");

            // Iteriert über alle ausgehenden Kanten des aktuellen Knotens.
            for (Edge<K, W> edge : adjacencyList.get(node)) {
                // Fügt das Ziel des Kantenziels (target) und dessen Gewicht in das String-Format ein.
                sb.append(edge.target).append("(").append(edge.weight).append("), ");
            }

            // Fügt nach jedem Knoten eine neue Zeile hinzu.
            sb.append("\n");
        }

        // Gibt die Darstellung des Graphen als String zurück.
        return sb.toString();
    }

    /**
     * Innere Klasse zur Darstellung einer Kante im Graphen.
     * Eine Kante hat einen Zielknoten (target) und ein Gewicht (weight).
     *
     * @param <K> Der Typ des Zielknotens (z. B. String oder Integer).
     * @param <W> Der Typ des Gewichtes (z. B. Integer oder Double).
     */
    private static class Edge<K, W> {
        // Der Zielknoten dieser Kante.
        public final K target;
        // Das Gewicht dieser Kante.
        public final W weight;

        /**
         * Konstruktor zur Initialisierung einer Kante mit einem Zielknoten und einem Gewicht.
         *
         * @param target Der Zielknoten der Kante.
         * @param weight Das Gewicht der Kante.
         */
        public Edge(K target, W weight) {
            this.target = target; // Initialisiert den Zielknoten.
            this.weight = weight; // Initialisiert das Gewicht der Kante.
        }
    }

    /**
     * Innere Klasse zur Darstellung eines Paares aus einem Knoten und seiner Distanz.
     * Diese Klasse wird insbesondere für die Prioritätswarteschlange im Dijkstra-Algorithmus verwendet.
     *
     * @param <K> Der Typ des Knotens.
     */
    private static class Pair<K> {
        // Der Knoten, der in diesem Paar repräsentiert wird.
        public final K node;
        // Die Distanz dieses Knotens (z. B. vom Startknoten aus).
        public final double distance;

        /**
         * Konstruktor zur Initialisierung eines Paares mit einem Knoten und einer Distanz.
         *
         * @param node Der Knoten, der in diesem Paar gespeichert wird.
         * @param distance Die Distanz des Knotens.
         */
        public Pair(K node, double distance) {
            this.node = node; // Initialisiert den Knoten.
            this.distance = distance; // Initialisiert die Distanz des Knotens.
        }
    }

    /**
     * Gibt die Liste der ausgehenden Kanten eines Knotens zurück.
     * @param node Der Knoten, dessen Kanten zurückgegeben werden sollen.
     * @return Eine Liste der ausgehenden Kanten oder eine leere Liste, falls der Knoten nicht existiert.
     */
    public List<Edge<K, W>> getEdges(K node) {
        // Gibt die Kanten des Knotens zurück oder eine leere Liste, falls der Knoten nicht existiert
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    /**
     * Gibt alle Knoten des Graphen zurück.
     * @return Eine Menge mit allen Knoten des Graphen.
     */
    public Set<K> getNodes() {
        // Gibt die Schlüssel (Knoten) der Adjazenzliste zurück
        return adjacencyList.keySet();
    }
}
