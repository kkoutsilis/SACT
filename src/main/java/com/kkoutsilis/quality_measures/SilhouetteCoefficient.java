package com.kkoutsilis.quality_measures;

import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;

import java.util.*;

public class SilhouetteCoefficient {
    private List<Set<Vertex>> clusteringResult;
    private Graph graph;

    public SilhouetteCoefficient() {
    }

    public SilhouetteCoefficient(List<Set<Vertex>> clusteringResult, Graph graph) {
        this.clusteringResult = clusteringResult;
        this.graph = graph;
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
                sum += this.dist(i.getLabel(), j.getLabel());
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
                    sum += this.dist(i.getLabel(), j.getLabel());
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
            res = 1 - (a / b);
        } else if (a > b) {
            res = (b / a) - 1;
        } else {
            res = 0;
        }
        return res;
    }

    public int dist(int source, int dest) {
        int nOfVertices = this.graph.getVertices().size() + 1;
        PriorityQueue<Vertex> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(Vertex::getLabel));
        minHeap.add(new Vertex(source));

        List<Integer> dist;
        dist = new ArrayList<>(Collections.nCopies(nOfVertices, Integer.MAX_VALUE));

        dist.set(source, 0);

        boolean[] done = new boolean[nOfVertices];
        done[source] = true;

        int[] prev = new int[nOfVertices];
        prev[source] = -1;

        while (!minHeap.isEmpty()) {
            // Remove and return the best vertex
            Vertex vertex = minHeap.poll();

            int u = vertex.getLabel();

            for (Vertex edge : graph.getEdges(vertex)) {
                int v = edge.getLabel();
                int weight = 1;

                if (!done[v] && (dist.get(u) + weight) < dist.get(v)) {
                    dist.set(v, dist.get(u) + weight);
                    prev[v] = u;
                    minHeap.add(new Vertex(v));
                }
            }
            done[u] = true;
        }
        return dist.get(dest);
    }
}
