package org.example;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class App {
    public static void main(String[] args) throws Exception {
        String inputFilePath = args[0];
        String outputFilePath = args[1];
        int k = Integer.parseInt(args[2]);
        int nearestNeighbourAlgorithm = Integer.parseInt(args[3]);
        int n = Integer.parseInt(args[4]);

        Graph graph = CsvHandler.parseCSV(inputFilePath);

        Map<Vertex, Set<Vertex>> knnRes = NearestNeighbour.knn(k, graph);

        Map<Vertex, Set<Vertex>> nnAlgorithm;
        if (nearestNeighbourAlgorithm == 1) {
            nnAlgorithm = NearestNeighbour.rknn(knnRes);

        } else if (nearestNeighbourAlgorithm == 2) {
            Map<Vertex, Set<Vertex>> rKnnRes = NearestNeighbour.rknn(knnRes);
            nnAlgorithm = NearestNeighbour.mknn(knnRes, rKnnRes);

        } else {
            throw new IllegalArgumentException("Type 1 for rKNN or 2 for mKNN");
        }

        HCNN algo = new HCNN(graph, n, knnRes, nnAlgorithm);
        List<Set<Vertex>> result = algo.fit();

        CsvHandler.dumpToCSV(outputFilePath, result);
    }
}
