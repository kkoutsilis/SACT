package org.example;

import java.util.*;

public class App {
    public static void main(String[] args) {

        Graph graph = testGraph3();
        Map<String, Set<String>> a = knn(3, graph);
        Map<String, Set<String>> b = rknn(a);
        Map<String, Set<String>> c = mknn(a, b);


        System.out.println("kkn " + a);
        System.out.println("rknn " + b);
        System.out.println("mknn " + c);
    }

    // return the nearest neighbours for k levels for one vertex
    public static Map<Integer, List<String>> knnLevels(int k, String root, Graph graph) {
        Map<Integer, List<String>> nearestNeighbours = new HashMap<>();
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();
        Queue<Integer> levels = new LinkedList<>();

        visited.add(root);
        queue.add(root);
        levels.add(1);
        while (!queue.isEmpty()) {
            int level = levels.poll();
            if (level > k) {
                break;
            }
            String vertex = queue.poll();
            nearestNeighbours.putIfAbsent(level, new ArrayList<>());
            for (Vertex v : graph.getEdges(vertex)) {
                if (!visited.contains(v.getLabel())) {
                    visited.add(v.getLabel());
                    queue.add(v.getLabel());
                    levels.add(level + 1);
                    nearestNeighbours.get(level).add(v.getLabel());
                }
            }
        }
        if (nearestNeighbours.get(nearestNeighbours.size()).isEmpty()) {
            nearestNeighbours.remove(nearestNeighbours.size());
        }
        return nearestNeighbours;
    }

    // return the k nearest neighbours for each vertex of a graph
    public static Map<String, Set<String>> knn(int k, Graph graph) {
        Map<String, Set<String>> nearestNeighbours = new HashMap<>();
        Set<String> indexes = new LinkedHashSet<>();
        graph.getVertices().keySet().forEach(vertex -> indexes.add(vertex.getLabel()));
        for (String index : indexes) {
            Set<String> visited = new LinkedHashSet<>();
            Set<String> neighbours = new LinkedHashSet<>();
            Queue<String> queue = new LinkedList<>();
            queue.add(index);
            visited.add(index);
            while (!queue.isEmpty() && neighbours.size() < k) {
                String vertex = queue.poll();
                for (Vertex v : graph.getEdges(vertex)) {
                    if (!visited.contains(v.getLabel()) && neighbours.size() < k) {
                        visited.add(v.getLabel());
                        neighbours.add(v.getLabel());
                        queue.add(v.getLabel());
                    }
                }
            }
            nearestNeighbours.put(index, neighbours);
        }
        return nearestNeighbours;
    }

    // reverse nearest neighbours of knn
    public static Map<String, Set<String>> rknn(Map<String, Set<String>> knn) {
        Map<String, Set<String>> reverseNearestNeighbours = new HashMap<>();
        Set<String> D = knn.keySet();
        for (String i : D) {
            reverseNearestNeighbours.put(i, new LinkedHashSet<>());
            for (String j : D) {
                if (knn.get(j).contains(i)) {
                    reverseNearestNeighbours.get(i).add(j);
                }
            }
        }
        return reverseNearestNeighbours;
    }

    public static Map<String, Set<String>> mknn(Map<String, Set<String>> knn, Map<String, Set<String>> rknn) {
        Map<String, Set<String>> mutualNearestNeighbours = new HashMap<>();
        Set<String> D = knn.keySet();
        for (String i : D) {
            Set<String> intersection = new LinkedHashSet<>(knn.get(i));
            intersection.retainAll(rknn.get(i));
            mutualNearestNeighbours.put(i, intersection);
        }
        return mutualNearestNeighbours;
    }


    public static Graph testGraph1() {
        Graph graph = new Graph();
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
        graph.addEdge("1", "2");
        graph.addEdge("2", "3");
        graph.addEdge("3", "4");
        graph.addEdge("4", "5");
        graph.addEdge("5", "6");
        graph.addEdge("6", "1");
        return graph;
    }

    public static Graph testGraph2() {
        Graph graph = new Graph();
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");

        graph.addEdge("1", "2");
        graph.addEdge("1", "4");

        graph.addEdge("2", "3");

        graph.addEdge("3", "2");
        graph.addEdge("3", "6");

        graph.addEdge("4", "5");

        graph.addEdge("6", "4");

        return graph;
    }

    public static Graph testGraph3() {
        Graph graph = new Graph();
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
        graph.addVertex("7");
        graph.addVertex("8");
        graph.addVertex("9");
        graph.addVertex("10");


        graph.addEdge("1", "2");
        graph.addEdge("1", "6");
        graph.addEdge("1", "8");

        graph.addEdge("2", "3");
        graph.addEdge("2", "6");
        graph.addEdge("2", "7");
        graph.addEdge("2", "9");

        graph.addEdge("3", "10");
        graph.addEdge("3", "4");


        graph.addEdge("4", "2");

        graph.addEdge("5", "3");
        graph.addEdge("5", "9");

        graph.addEdge("6", "10");

        graph.addEdge("7", "5");

        graph.addEdge("9", "2");
        graph.addEdge("9", "10");

        graph.addEdge("10", "1");

        return graph;
    }

    public static Graph testGraph4() {
        Graph graph = new Graph();
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");

        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.addEdge("1", "4");
        graph.addEdge("1", "5");
        graph.addEdge("1", "6");

        graph.addEdge("2", "1");
        graph.addEdge("2", "3");
        graph.addEdge("2", "4");
        graph.addEdge("2", "5");
        graph.addEdge("2", "6");

        graph.addEdge("3", "2");
        graph.addEdge("3", "1");
        graph.addEdge("3", "4");
        graph.addEdge("3", "5");
        graph.addEdge("3", "6");

        graph.addEdge("4", "2");
        graph.addEdge("4", "3");
        graph.addEdge("4", "1");
        graph.addEdge("4", "5");
        graph.addEdge("4", "6");

        graph.addEdge("5", "2");
        graph.addEdge("5", "3");
        graph.addEdge("5", "4");
        graph.addEdge("5", "1");
        graph.addEdge("5", "6");

        graph.addEdge("6", "2");
        graph.addEdge("6", "3");
        graph.addEdge("6", "4");
        graph.addEdge("6", "5");
        graph.addEdge("6", "1");
        return graph;
    }
}
