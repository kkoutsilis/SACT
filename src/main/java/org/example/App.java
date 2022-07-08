package org.example;

import java.util.*;


public class App {
    public static void main(String[] args) throws Exception {

        Graph graph = new Graph();
        graph.parseCSV("CSVs/test.csv");
        Map<Vertex, Set<Vertex>> knnRes = knn(3, graph);
        Map<Vertex, Set<Vertex>> rknnRes = rknn(knnRes);
        Map<Vertex, Set<Vertex>> mknnRes = mknn(knnRes, rknnRes);

        HCNN algo = new HCNN(graph, 5, knnRes, rknnRes);
        List<Set<Vertex>> res = algo.fit();
        for (Set<Vertex> s : res) {
            System.out.println("------------------------------CLUSTER------------------------------");
            System.out.println(s);
            System.out.println();
        }

    }

    // return the k nearest neighbours for each vertex of a graph
    public static Map<Vertex, Set<Vertex>> knn(int k, Graph graph) {
        Map<Vertex, Set<Vertex>> nearestNeighbours = new HashMap<>();
        Set<Vertex> indexes = new LinkedHashSet<>(graph.getVertices().keySet());
        for (Vertex index : indexes) {
            Set<Vertex> visited = new LinkedHashSet<>();
            Set<Vertex> neighbours = new LinkedHashSet<>();
            Queue<Vertex> queue = new LinkedList<>();
            queue.add(index);
            visited.add(index);
            while (!queue.isEmpty() && neighbours.size() < k) {
                Vertex vertex = queue.poll();
                for (Vertex v : graph.getEdges(vertex.getLabel())) {
                    if (!visited.contains(v) && neighbours.size() < k) {
                        visited.add(v);
                        neighbours.add(v);
                        queue.add(v);
                    }
                }
            }
            nearestNeighbours.put(index, neighbours);
        }
        return nearestNeighbours;
    }

    // reverse nearest neighbours of knn
    public static Map<Vertex, Set<Vertex>> rknn(Map<Vertex, Set<Vertex>> knn) {
        Map<Vertex, Set<Vertex>> reverseNearestNeighbours = new HashMap<>();
        Set<Vertex> D = knn.keySet();
        for (Vertex i : D) {
            reverseNearestNeighbours.put(i, new LinkedHashSet<>());
            for (Vertex j : D) {
                if (knn.get(j).contains(i)) {
                    reverseNearestNeighbours.get(i).add(j);
                }
            }
        }
        return reverseNearestNeighbours;
    }

    public static Map<Vertex, Set<Vertex>> mknn(Map<Vertex, Set<Vertex>> knn, Map<Vertex, Set<Vertex>> rknn) {
        Map<Vertex, Set<Vertex>> mutualNearestNeighbours = new HashMap<>();
        Set<Vertex> D = knn.keySet();
        for (Vertex i : D) {
            Set<Vertex> intersection = new LinkedHashSet<>(knn.get(i));
            intersection.retainAll(rknn.get(i));
            mutualNearestNeighbours.put(i, intersection);
        }
        return mutualNearestNeighbours;
    }
}
