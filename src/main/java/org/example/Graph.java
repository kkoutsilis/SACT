package org.example;

import java.util.*;

public class Graph {
    private final Map<Vertex, Set<Vertex>> vertices;


    public Graph() {
        this.vertices = new HashMap<>();
    }

    public Graph(Map<Vertex, Set<Vertex>> vertices) {
        this.vertices = vertices;
    }

    public void addVertex(int label) {
        vertices.putIfAbsent(new Vertex(label), new LinkedHashSet<>());
    }

    public void removeVertex(int label) {
        Vertex v = new Vertex(label);
        vertices.values().forEach(e -> e.remove(v));
        vertices.remove(v);
    }

    public void addEdge(int label1, int label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        vertices.get(v1).add(v2);
    }

    public void removeEdge(int label1, int label2) {
        Vertex v1 = new Vertex(label1);
        Vertex v2 = new Vertex(label2);
        Set<Vertex> eV1 = vertices.get(v1);
        if (eV1 != null)
            eV1.remove(v2);

    }

    public Map<Vertex, Set<Vertex>> getVertices() {
        return this.vertices;
    }

    public Set<Vertex> getEdges(int label) {
        return vertices.get(new Vertex(label));
    }

    public void printVertices() {
        for (Vertex v : this.vertices.keySet()) {
            System.out.print("{" + v.getLabel());
            System.out.print("->");
            System.out.println(this.vertices.get(v));
        }
    }

    @Override
    public String toString() {
        return "Graph{" +
                "vertices=" + vertices +
                '}';
    }
}
