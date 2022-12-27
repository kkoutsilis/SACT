package com.kkoutsilis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.kkoutsilis.algorithms.ClusteringAlgorithm;
import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;
import com.kkoutsilis.menus.HcnnMenu;
import com.kkoutsilis.menus.IMenu;
import com.kkoutsilis.quality_measures.SilhouetteCoefficient;
import com.kkoutsilis.utilities.CsvHandler;

/**
 * @author kkoutsilis
 */
public class App {
    public static void main(String[] args) throws Exception {
        Map<String, IMenu> menuOptions = Map.of("HCNN", new HcnnMenu());
        final Logger logger = Logger.getLogger(App.class.getName());
        final String inputFilePath = args[0];
        final String outputFilePath = args[1];
        final String selectedAlgo = args[2];

        long startTime = System.nanoTime();

        Map<Vertex, Set<Vertex>> inputVertices = CsvHandler.parseCSV(inputFilePath);

        Graph graph = new Graph(inputVertices);

        ClusteringAlgorithm algo;

        IMenu menu = menuOptions.getOrDefault(selectedAlgo, null);
        if (menu == null) {
            throw new Exception("Selected algorithm not implemented yet!");
        }
        algo = menu.option(graph, args);

        List<Set<Vertex>> result = algo.fit();

        CsvHandler.dumpToCSV(outputFilePath, result);
        long elapsedTime = System.nanoTime() - startTime;
        int i = 0;
        for (Set<Vertex> s : result) {
            System.out.printf("------------------------------CLUSTER%d------------------------------%n", i++);
            s.forEach(v -> System.out.print(v.getLabel() + " "));
            System.out.println();
        }
        logger.log(Level.INFO, () -> "Clustering execution time in milliseconds: " + elapsedTime / 1000000);

        SilhouetteCoefficient quality = new SilhouetteCoefficient(result, graph);
        logger.log(Level.INFO, () -> String.format("Silhouette Coefficient: %.03f", quality.calculate()));
    }
}
