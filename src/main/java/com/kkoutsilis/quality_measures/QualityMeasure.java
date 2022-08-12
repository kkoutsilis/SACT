package com.kkoutsilis.quality_measures;

import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;

import java.util.List;
import java.util.Set;

public abstract class QualityMeasure {
    final List<Set<Vertex>> clusteringResult;
    final Graph graph;

    protected QualityMeasure(List<Set<Vertex>> clusteringResult, Graph graph) {
        this.clusteringResult = clusteringResult;
        this.graph = graph;
    }

    abstract float calculate();
}
