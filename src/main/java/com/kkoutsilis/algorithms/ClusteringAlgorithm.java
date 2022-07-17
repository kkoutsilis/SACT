package com.kkoutsilis.algorithms;

import com.kkoutsilis.graphs.Vertex;

import java.util.List;
import java.util.Set;

public interface ClusteringAlgorithm {
    List<Set<Vertex>> fit() throws Exception;
}
