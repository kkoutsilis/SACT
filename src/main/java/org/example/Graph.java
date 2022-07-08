//package org.example;
//
//import com.opencsv.CSVReader;
//import com.opencsv.CSVReaderBuilder;
//import com.opencsv.exceptions.CsvException;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.*;
//
//public class Graph {
//    private final Map<Vertex, Set<Vertex>> vertices;
//
//
//    public Graph() {
//        this.vertices = new HashMap<>();
//    }
//
//    public Graph(Map<Vertex, Set<Vertex>> vertices) {
//        this.vertices = vertices;
//    }
//
//    public void parseCSV(String path) throws IllegalStateException {
//        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withSkipLines(1)           // skip the first line, header info
//                .build()) {
//            List<String[]> r = reader.readAll();
//            for (String[] row : r) {
//                int v1 = Integer.parseInt(row[2]);
//                int v2 = Integer.parseInt(row[5]);
//                this.addVertex(v1);
//                this.addVertex(v2);
//                this.addEdge(v1, v2);
//
//            }
//        } catch (IOException | CsvException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void addVertex(int label) {
//        vertices.putIfAbsent(new Vertex(label), new LinkedHashSet<>());
//    }
//
//    public void removeVertex(int label) {
//        Vertex v = new Vertex(label);
//        vertices.values().forEach(e -> e.remove(v));
//        vertices.remove(v);
//    }
//
//    public void addEdge(int label1, int label2) {
//        Vertex v1 = new Vertex(label1);
//        Vertex v2 = new Vertex(label2);
//        vertices.get(v1).add(v2);
//    }
//
//    public void removeEdge(int label1, int label2) {
//        Vertex v1 = new Vertex(label1);
//        Vertex v2 = new Vertex(label2);
//        Set<Vertex> eV1 = vertices.get(v1);
//        if (eV1 != null) eV1.remove(v2);
//
//    }
//
//    public Map<Vertex, Set<Vertex>> getVertices() {
//        return this.vertices;
//    }
//
//    public Set<Vertex> getEdges(int label) {
//        return vertices.get(new Vertex(label));
//    }
//
//    public void printVertices() {
//        for (Vertex v : this.vertices.keySet()) {
//            System.out.print("{" + v.getLabel());
//            System.out.print("->");
//            System.out.println(this.vertices.get(v));
//        }
//    }
//
//    @Override
//    public String toString() {
//        return "Graph{" + "vertices=" + vertices + '}';
//    }
//}
package org.example;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
    private final Map<Vertex, Set<Vertex>> vertices;


    public Graph() {
        this.vertices = new HashMap<>();
    }

    public Graph(Map<Vertex, Set<Vertex>> vertices) {
        this.vertices = vertices;
    }

    public void parseCSV(String path) throws IllegalStateException {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withSkipLines(1)           // skip the first line, header info
                .build()) {
            List<String[]> r = reader.readAll();
            for (String[] row : r) {
                Vertex v1 = new Vertex(Integer.parseInt(row[2]));
                Vertex v2 = new Vertex(Integer.parseInt(row[5]));
                this.addVertex(v1);
                this.addVertex(v2);
                this.addEdge(v1, v2);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
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
