package org.example;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
    private final Map<Vertex, Set<Vertex>> vertices;


    public Graph() {
        this.vertices = new HashMap<>();
    }

    public Graph(Map<Vertex, Set<Vertex>> vertices) {
        this.vertices = vertices;
    }

    public void addVertex(Vertex vertex) {
        vertices.putIfAbsent(vertex, new LinkedHashSet<>());
    }

    public void removeVertex(Vertex vertex) {
        vertices.values().forEach(e -> e.remove(vertex));
        vertices.remove(vertex);
    }

    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertices.get(vertex1).add(vertex2);
    }

    public void removeEdge(Vertex vertex1, Vertex vertex2) {
        Set<Vertex> eV1 = vertices.get(vertex1);
        if (eV1 != null) eV1.remove(vertex2);

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
        return "Graph{" + "vertices=" + vertices + '}';
    }
}
