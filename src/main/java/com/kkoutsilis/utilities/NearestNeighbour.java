package com.kkoutsilis.utilities;

import com.kkoutsilis.graphs.Vertex;
import com.kkoutsilis.graphs.Graph;

import java.util.*;

public final class NearestNeighbour {
    private NearestNeighbour() {
        throw new IllegalStateException("Utility class");

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
                for (Vertex v : graph.getEdges(vertex)) {
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
