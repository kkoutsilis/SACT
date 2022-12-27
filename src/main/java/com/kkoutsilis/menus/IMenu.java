package com.kkoutsilis.menus;

import com.kkoutsilis.algorithms.ClusteringAlgorithm;
import com.kkoutsilis.graphs.Graph;

public interface IMenu {
    public ClusteringAlgorithm option(Graph graph, String[] args) throws Exception;
}
