package org.example;

import java.util.*;

public class App {
    public static void main(String[] args) {

        Graph graph = testGraph3();
        System.out.println(knn(3, "5", graph));
    }

    // TODO find k nn not all of them
    public static Map<Integer, List<String>> knn(int k, String root, Graph graph) {
        Map<Integer, List<String>> nearestNeighbours = new HashMap<>();
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();
        Queue<Integer> levels = new LinkedList<>();

        visited.add(root);
        queue.add(root);
        levels.add(1);
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            int level = levels.poll();
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


    public static Set<String> breadthFirstTraversal(Graph graph, String root) {
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            for (Vertex v : graph.getEdges(vertex)) {
                if (!visited.contains(v.getLabel())) {
                    visited.add(v.getLabel());
                    queue.add(v.getLabel());
                }
            }
        }
        return visited;
    }

    public static Set<String> depthFirstTraversal(Graph graph, String root) {
        Set<String> visited = new LinkedHashSet<>();
        Stack<String> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Vertex v : graph.getEdges(vertex)) {
                    stack.push(v.getLabel());
                }
            }
        }
        return visited;
    }
}
