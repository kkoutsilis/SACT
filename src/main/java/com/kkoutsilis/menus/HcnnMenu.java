package com.kkoutsilis.menus;

import com.kkoutsilis.algorithms.ClusteringAlgorithm;
import com.kkoutsilis.algorithms.HCNN;
import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;
import com.kkoutsilis.utilities.NearestNeighbour;


import java.util.Map;
import java.util.Set;


public class HcnnMenu implements IMenu {

    public ClusteringAlgorithm option(Graph graph, String[] args) throws Exception {
        final int k = Integer.parseInt(args[3]);
        final int fistNearestNeighbourAlgorithm = Integer.parseInt(args[4]);
        final int secondNearestNeighbourAlgorithm = Integer.parseInt(args[5]);
        final int n = Integer.parseInt(args[6]);

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

        ClusteringAlgorithm algorithm = new HCNN(graph, n, fistNnAlgorithm, secondNnAlgorithm);

        return algorithm;
    }

}
