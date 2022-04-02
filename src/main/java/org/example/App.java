package org.example;

import java.util.*;

public class App {
    public static void main(String[] args) {

        Graph graph = testGraph3();
        Map<Vertex,Set<Vertex>> knnRes = knn(3,graph);
        System.out.println("--------KNN---------");
        knnRes.forEach((vertex, neighbours) -> {
            System.out.print(vertex.getLabel() + " -> ");
            neighbours.forEach(v -> System.out.print(v.getLabel() + " "));
            System.out.println();
        });
        Map<Vertex,Set<Vertex>> rknnRes = rknn(knnRes);
        System.out.println("--------rKNN---------");
        rknnRes.forEach((vertex, neighbours) -> {
            System.out.print(vertex.getLabel() + " -> ");
            neighbours.forEach(v -> System.out.print(v.getLabel() + " "));
            System.out.println();
        });
        Map<Vertex,Set<Vertex>> mknnRes = mknn(knnRes,rknnRes);
        System.out.println("--------mKNN---------");
        mknnRes.forEach((vertex, neighbours) -> {
            System.out.print(vertex.getLabel() + " -> ");
            neighbours.forEach(v -> System.out.print(v.getLabel() + " "));
            System.out.println();
        });
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


    public static Graph testGraph1() {
        Graph graph = new Graph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);
        graph.addEdge(6, 1);
        return graph;
    }

    public static Graph testGraph2() {
        Graph graph = new Graph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);

        graph.addEdge(1, 2);
        graph.addEdge(1, 4);

        graph.addEdge(2, 3);

        graph.addEdge(3, 2);
        graph.addEdge(3, 6);

        graph.addEdge(4, 5);

        graph.addEdge(6, 4);

        return graph;
    }

    public static Graph testGraph3() {
        Graph graph = new Graph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);
        graph.addVertex(7);
        graph.addVertex(8);
        graph.addVertex(9);
        graph.addVertex(10);


        graph.addEdge(1, 2);
        graph.addEdge(1, 6);
        graph.addEdge(1, 8);

        graph.addEdge(2, 3);
        graph.addEdge(2, 6);
        graph.addEdge(2, 7);
        graph.addEdge(2, 9);

        graph.addEdge(3, 10);
        graph.addEdge(3, 4);


        graph.addEdge(4, 2);

        graph.addEdge(5, 3);
        graph.addEdge(5, 9);

        graph.addEdge(6, 10);

        graph.addEdge(7, 5);

        graph.addEdge(9, 2);
        graph.addEdge(9, 10);

        graph.addEdge(10, 1);

        return graph;
    }

    public static Graph testGraph4() {
        Graph graph = new Graph();
        graph.addVertex(1);
        graph.addVertex(2);
        graph.addVertex(3);
        graph.addVertex(4);
        graph.addVertex(5);
        graph.addVertex(6);

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(1, 5);
        graph.addEdge(1, 6);

        graph.addEdge(2, 1);
        graph.addEdge(2, 3);
        graph.addEdge(2, 4);
        graph.addEdge(2, 5);
        graph.addEdge(2, 6);

        graph.addEdge(3, 2);
        graph.addEdge(3, 1);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        graph.addEdge(3, 6);

        graph.addEdge(4, 2);
        graph.addEdge(4, 3);
        graph.addEdge(4, 1);
        graph.addEdge(4, 5);
        graph.addEdge(4, 6);

        graph.addEdge(5, 2);
        graph.addEdge(5, 3);
        graph.addEdge(5, 4);
        graph.addEdge(5, 1);
        graph.addEdge(5, 6);

        graph.addEdge(6, 2);
        graph.addEdge(6, 3);
        graph.addEdge(6, 4);
        graph.addEdge(6, 5);
        graph.addEdge(6, 1);
        return graph;
    }
}
