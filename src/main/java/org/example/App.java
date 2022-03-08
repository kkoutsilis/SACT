package org.example;

import java.util.*;

public class App {
    public static void main(String[] args) throws Exception {
//        Representative rep1 = DisjointSets.makeSet(1);
//        Representative rep2 = DisjointSets.makeSet(2);
//        DisjointSets.union(1, 2);
//        Representative rep3 = DisjointSets.makeSet(3);
//        Representative rep4 = DisjointSets.makeSet(4);
//        DisjointSets.union(3, 4);
//        Representative rep5 = DisjointSets.makeSet(5);
//        Representative rep6 = DisjointSets.makeSet(6);
//        DisjointSets.union(5, 6);
//        DisjointSets.union(1, 3);
//        DisjointSets.printSets();

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
        graph.addEdge("6", "3");
        graph.addEdge("6", "2");
        System.out.println(depthFirstTraversal(graph, "6"));

    }

    // SEPARATE TO LEVELS
    public static Set<String> depthFirstTraversal(Graph graph, String root) {
        Set<String> visited = new LinkedHashSet<String>();
        Stack<String> stack = new Stack<String>();
        stack.push(root);
        int i = 0;
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            System.out.print("k nearest neighbour " + ++i + ": ");
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Vertex v : graph.getAdjVertices(vertex)) {
                    System.out.print(v.getLabel() + ", ");
                    stack.push(v.getLabel());
                }
                System.out.println();
            }
            System.out.println();

        }
        return visited;
    }

    public static Set<String> breadthFirstTraversal(Graph graph, String root) {
        Set<String> visited = new LinkedHashSet<String>();
        Queue<String> queue = new LinkedList<String>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            String vertex = queue.poll();
            for (Vertex v : graph.getAdjVertices(vertex)) {
                if (!visited.contains(v.getLabel())) {
                    visited.add(v.getLabel());
                    queue.add(v.getLabel());
                }
            }
        }
        return visited;
    }
}
