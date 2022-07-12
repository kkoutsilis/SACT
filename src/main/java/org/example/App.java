package org.example;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) throws Exception {
        Graph graph = CsvHandler.parseCSV("CSVs/test.csv");

        Map<Vertex, Set<Vertex>> knnRes = NearestNeighbour.knn(5, graph);
        Map<Vertex, Set<Vertex>> rKnnRes = NearestNeighbour.rknn(knnRes);
        Map<Vertex, Set<Vertex>> mKnnRes = NearestNeighbour.mknn(knnRes, rKnnRes);

        HCNN algo = new HCNN(graph, 10, knnRes, rKnnRes);
        List<Set<Vertex>> res = algo.fit();

        CsvHandler.dumpToCSV("Out/res3.csv", res);
    }
}
