package com.kkoutsilis.utilities;

import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;

import java.util.*;

/**
 * Dijkstra's algorithm
 */
public final class Distance {

    private Distance() {
        throw new IllegalStateException("Utility class");
    }


    public static int calculate(int source, int dest, Graph graph) {
        int nOfVertices = graph.getVertices().size() + 1;
        PriorityQueue<Vertex> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(Vertex::getLabel));

        Vertex sourceVertex = graph.getVertices().keySet().stream().filter(v -> v.getLabel() == source).findFirst().orElse(null);
        minHeap.add(sourceVertex);

        List<Integer> dist;
        dist = new ArrayList<>(Collections.nCopies(nOfVertices, Integer.MAX_VALUE));

        dist.set(source, 0);

        boolean[] done = new boolean[nOfVertices];
        done[source] = true;

        while (!minHeap.isEmpty()) {
            Vertex vertex = minHeap.poll();
            int u = vertex.getLabel();
            for (Vertex edge : graph.getEdges(vertex)) {
                int v = edge.getLabel();
                if (!done[v] && (dist.get(u)) < dist.get(v)) {
                    dist.set(v, dist.get(u));
                    minHeap.add(edge);
                }
            }
            done[u] = true;
        }
        return dist.get(dest);
    }

}
