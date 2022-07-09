package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.opencsv.CSVWriter;

public class App {
    public static void main(String[] args) throws Exception {

        Graph graph = new Graph();
        graph.parseCSV("CSVs/test.csv");
        Map<Vertex, Set<Vertex>> knnRes = knn(5, graph);
        Map<Vertex, Set<Vertex>> rknnRes = rknn(knnRes);
        Map<Vertex, Set<Vertex>> mknnRes = mknn(knnRes, rknnRes);

        HCNN algo = new HCNN(graph, 10, knnRes, rknnRes);
        List<Set<Vertex>> res = algo.fit();
        dumpToCSV("Out/res1.csv", res);
    }

    public static void dumpToCSV(String path, List<Set<Vertex>> data) {
        File file = new File(path);
        try {
            FileWriter outputfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outputfile);

            String[] header = { "CLUSTER_ID", "SCHEMA", "NAME", "TYPE" };
            writer.writeNext(header);

            int clusterID = 0;
            for (Set<Vertex> vertexSet : data) {
                for (Vertex vertex : vertexSet) {
                    String[] output = { Integer.toString(clusterID), vertex.getSchema(),
                            Integer.toString(vertex.getLabel()), vertex.getType() };
                    writer.writeNext(output);
                }
                clusterID++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
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
