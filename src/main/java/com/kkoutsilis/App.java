package com.kkoutsilis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kkoutsilis.algorithms.ClusteringAlgorithm;
import com.kkoutsilis.algorithms.HCNN;
import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;
import com.kkoutsilis.quality_measures.SilhouetteCoefficient;
import com.kkoutsilis.utilities.CsvHandler;
import com.kkoutsilis.utilities.NearestNeighbour;

public class App {
    public static void main(String[] args) throws Exception {
        final Logger logger = Logger.getLogger(App.class.getName());
        final String inputFilePath = args[0];
        final String outputFilePath = args[1];
        final int k = Integer.parseInt(args[2]);
        final int fistNearestNeighbourAlgorithm = Integer.parseInt(args[3]);
        final int secondNearestNeighbourAlgorithm = Integer.parseInt(args[4]);
        final int n = Integer.parseInt(args[5]);

        long startTime = System.nanoTime();

        Map<Vertex, Set<Vertex>> inputVertices = CsvHandler.parseCSV(inputFilePath);

        Graph graph = new Graph(inputVertices);

        Map<Vertex, Set<Vertex>> knn = NearestNeighbour.knn(k, graph);

        Map<Vertex, Set<Vertex>> fistNnAlgorithm;
        if (fistNearestNeighbourAlgorithm == 1) {
            fistNnAlgorithm = knn;

        } else if (fistNearestNeighbourAlgorithm == 2) {
            Map<Vertex, Set<Vertex>> rKnnRes = NearestNeighbour.rknn(knn);
            fistNnAlgorithm = NearestNeighbour.mknn(knn, rKnnRes);

        } else {
            throw new IllegalArgumentException("Type 1 for KNN or 2 for mKNN");
        }
        Map<Vertex, Set<Vertex>> secondNnAlgorithm;
        if (secondNearestNeighbourAlgorithm == 1) {
            secondNnAlgorithm = knn;

        } else if (secondNearestNeighbourAlgorithm == 2) {
            Map<Vertex, Set<Vertex>> rKnnRes = NearestNeighbour.rknn(knn);
            secondNnAlgorithm = NearestNeighbour.mknn(knn, rKnnRes);

        } else if (secondNearestNeighbourAlgorithm == 3) {
            secondNnAlgorithm = NearestNeighbour.rknn(knn);

        } else {
            throw new IllegalArgumentException("Type 1 for KNN, 2 for mKNN or 3 for rKNN");
        }

        if (fistNearestNeighbourAlgorithm == 1 && secondNearestNeighbourAlgorithm == 3) {
            throw new IllegalArgumentException("KNN cannot be combined with rKNN");
        }

        ClusteringAlgorithm algo = new HCNN(graph, n, fistNnAlgorithm, secondNnAlgorithm);
        List<Set<Vertex>> result = algo.fit();

        CsvHandler.dumpToCSV(outputFilePath, result);
        long elapsedTime = System.nanoTime() - startTime;
        int i = 0;
        for (Set<Vertex> s : result) {
            System.out.printf("------------------------------CLUSTER%d------------------------------%n", i++);
            s.forEach(v -> System.out.print(v.getLabel() + " "));
            System.out.println();
        }
        logger.log(Level.INFO,
                () -> "Clustering execution time in milliseconds: " + elapsedTime / 1000000);

        SilhouetteCoefficient quality = new SilhouetteCoefficient(result, graph);
        logger.log(Level.INFO, () -> String.format("Silhouette Coefficient: %.03f", quality.calculate()));
    }
}
