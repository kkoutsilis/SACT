package org.example;

import org.example.sets.DisjointSets;
import org.example.sets.Representative;

import java.util.*;

public class App {
    public static void main(String[] args) {

        Graph graph = testGraph2();
        System.out.println(knn(3, "1", graph));
    }


    public static Map<Integer, List<String>> knn(int k, String root, Graph graph) {
        Map<Integer, List<String>> nearestNeighbours = new HashMap<>();
        Set<String> visited = new LinkedHashSet<>();
        Stack<String> stack = new Stack<>();
        stack.push(root);
        int i = 1;
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                nearestNeighbours.put(i, new ArrayList<>());
                for (Vertex v : graph.getEdges(vertex)) {
                    if (!visited.contains(v.getLabel())) {
                        stack.push(v.getLabel());
                        nearestNeighbours.get(i).add(v.getLabel());
                    }
                }
            }
            i ++;
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
