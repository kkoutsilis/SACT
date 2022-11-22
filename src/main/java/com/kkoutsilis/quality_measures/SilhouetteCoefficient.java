package com.kkoutsilis.quality_measures;

import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;
import com.kkoutsilis.utilities.Distance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author kkoutsilis
 */
public class SilhouetteCoefficient extends QualityMeasure {

    public SilhouetteCoefficient(List<Set<Vertex>> clusteringResult, Graph graph) {
        super(clusteringResult, graph);
    }

    public float calculate() {
        List<Float> sMean = new ArrayList<>();
        for (Set<Vertex> c : this.clusteringResult) {
            float sum = 0;
            for (Vertex v : c) {
                float a = this.a(v, c);
                float b = this.b(v, c);
                float s = this.s(a, b);
                sum += s;
            }
            sMean.add(sum / c.size());
        }
        return Collections.max(sMean);
    }

    private float a(Vertex i, Set<Vertex> cluster) {
        float sum = 0;
        for (Vertex j : cluster) {
            if (i != j) {
                sum += Distance.calculate(i.getLabel(), j.getLabel(), this.graph);
            }
        }
        return (1f / (cluster.size() - 1) * sum);
    }

    private float b(Vertex i, Set<Vertex> cluster) {
        float minB = Float.MAX_VALUE;
        for (Set<Vertex> c : this.clusteringResult) {
            if (c != cluster) {
                float sum = 0;
                for (Vertex j : c) {
                    sum += Distance.calculate(i.getLabel(), j.getLabel(), this.graph);
                }
                float res = (1f / c.size()) * sum;
                if (res < minB) {
                    minB = res;
                }
            }
        }
        return minB;
    }

    private float s(float a, float b) {
        float res;
        if (a < b) {
            res = 1f - (a / b);
        } else if (a > b) {
            res = (b / a) - 1f;
        } else {
            res = 0;
        }
        return res;
    }
}
