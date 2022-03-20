package org.example;

import java.util.*;

public class HCNN {
    private Graph graph;
    private int n;
    private Map<String,Set<String>> knn;
    private Map<String,Set<String>> rkkn;

    public HCNN() {
    }
    public HCNN(Graph graph, int n, Map<String,Set<String>> knn , Map<String,Set<String>> rknn){
        this.graph = graph;
        this.n = n;
        this.knn = knn;
        this.rkkn = rknn;
    }
    public Set<String> fit(){
        return Collections.emptySet();
    }

    // TODO types will probably have to change.
    private Map<String,Set<String>> assignOutliers(Map<String,Set<String>> clusters, Set<Integer> indexes, Set<Integer> outliers ){
        Map<String,Integer> label = new HashMap<>();

        for (int i=1; i <= clusters.size(); i++){
            for (String j : clusters.get(Integer.toString(i))){
                label.put(j,i);
            }
        }
        for (Integer o: outliers){
            int t = o;
            if (!this.knn.get(Integer.toString(o)).isEmpty()){
                int minStractSim = Integer.MAX_VALUE;
                for (String i : this.knn.get(Integer.toString(o))){
                   int stractSim = z(o, Integer.parseInt(i));
                   if (minStractSim > stractSim) {
                       minStractSim = stractSim;
                   }
               }
                t = minStractSim;
            }
            if (label.get(Integer.toString(t)) == 0){
                int minDist = Integer.MAX_VALUE;
                for(int i: indexes ){
                    if (label.get(Integer.toString(i)) >0){
                        int distance = this.dist(i,o);
                        if (minDist > distance){
                            minDist = distance;
                        }
                    }
                }
                t = minDist;
            }
            label.replace(Integer.toString(o),label.get(Integer.toString(t)));
            clusters.get(Integer.toString(label.get(Integer.toString(o)))).add(Integer.toString(o));
        }
        return clusters;
    }
     //TODO this will have to return 2 Sets, implement custom class to return 2 sets.
    private Set<String> initializeClustering(){
        return Collections.emptySet();
    }

    private int z(int i, int j){
        Set<String> intersection = new LinkedHashSet<>(this.knn.get(Integer.toString(i)));
        intersection.retainAll(this.knn.get(Integer.toString(j)));

        Set<String> union = new LinkedHashSet<>(knn.get(i));
        union.addAll(this.knn.get(Integer.toString(j)));

        return intersection.size()/union.size();

    }
    // TODO types will probably have to chage.
    private int conn(Set<String> clusterA, Set<String> clusterB){
        int connSum= 0;
        for (String a: clusterA){
            Set<String> intersection = new LinkedHashSet<>(this.knn.get(a));
            intersection.retainAll(clusterB);
            connSum += intersection.size();
        }

        for (String b: clusterB){
            Set<String> intersection = new LinkedHashSet<>(this.knn.get(b));
            intersection.retainAll(clusterA);
            connSum += intersection.size();
        }
        return connSum;
    }
    // TODO types will probably have to chage.
    private int link(Set<String> clusterA, Set<String> clusterB){
        Set<Set<String>> linkSet = new LinkedHashSet<>();
        for(String a:clusterA){
            Set<String> intersection = new LinkedHashSet<>(this.knn.get(a));
            intersection.retainAll(clusterB);
            if (!intersection.isEmpty()){
                linkSet.add(intersection);
            }
        }
        return linkSet.size();
    }
    // TODO types will probably have to chage.
    private  int sim(Set<String> clusterA, Set<String> clusterB){
        return (conn(clusterA,clusterB)/(clusterA.size() * clusterB.size())) * (link(clusterA,clusterB)/clusterA.size()) * (link(clusterB,clusterA)/clusterB.size());
    }
    // TODO this is calculating distances for all the vertices compared to source, refactor to find dist of source to destination vertex.
    public int dist(int source ,int dest) {
        int nOfVertices = this.graph.getVertices().size()+1;
        // create a min-heap and push source node having distance 0
        PriorityQueue<Vertex> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(vertex -> 1));
        minHeap.add(new Vertex(Integer.toString(source)));

        // set initial distance from the source to `v` as infinity
        List<Integer> dist;
        dist = new ArrayList<>(Collections.nCopies(nOfVertices, Integer.MAX_VALUE));

        // distance from the source to itself is zero
        dist.set(source, 0);

        // boolean array to track vertices for which minimum
        // cost is already found
        boolean[] done = new boolean[nOfVertices];
        done[source] = true;

        // stores predecessor of a vertex (to a print path)
        int[] prev = new int[nOfVertices];
        prev[source] = -1;

        // run till min-heap is empty
        while (!minHeap.isEmpty())
        {
            // Remove and return the best vertex
            Vertex vertex = minHeap.poll();

            // get the vertex number
            int u = Integer.parseInt(vertex.getLabel());

            // do for each neighbor `v` of `u`
            for (Vertex edge: this.graph.getEdges(Integer.toString(u)))
            {
                int v = Integer.parseInt(edge.getLabel());
                int weight = 1;

                // Relaxation step
                if (!done[v] && (dist.get(u) + weight) < dist.get(v))
                {
                    dist.set(v, dist.get(u) + weight);
                    prev[v] = u;
                    minHeap.add(new Vertex(Integer.toString(v)));
                }
            }
            // mark vertex `u` as done so it will not get picked up again
            done[u] = true;
        }
        return dist.get(dest);
    }
}
