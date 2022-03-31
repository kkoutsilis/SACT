package org.example;

import org.example.sets.DisjointSets;

import java.util.*;

class CorePair {
    private int i;
    private int j;

    public CorePair() {

    }

    public CorePair(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return this.i;
    }

    public int getJ() {
        return this.j;
    }


    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void setPair(int i, int j) {
        this.setI(i);
        this.setJ(j);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CorePair)) return false;
        CorePair corePair = (CorePair) o;
        return getI() == corePair.getI() && getJ() == corePair.getJ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getI(), getJ());
    }
}

class PairSim {
    private int i;
    private int j;
    private int similarity;

    public PairSim() {
    }

    public PairSim(int i, int j, int similarity) {
        this.i = i;
        this.j = j;
        this.similarity = similarity;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getSimilarity() {
        return similarity;
    }

    public void setSimilarity(int similarity) {
        this.similarity = similarity;
    }

    public void setPairSim(int i, int j, int similarity) {
        this.setI(i);
        this.setJ(j);
        this.setSimilarity(similarity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PairSim)) return false;
        PairSim pairSim = (PairSim) o;
        return getI() == pairSim.getI() && getJ() == pairSim.getJ() && getSimilarity() == pairSim.getSimilarity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getI(), getJ(), getSimilarity());
    }
}

class ClustersAndOutliers {
    private Map<String, Set<String>> clusters;
    private Set<String> outliers;

    public ClustersAndOutliers() {
    }

    public ClustersAndOutliers(Map<String, Set<String>> clusters, Set<String> outliers) {
        this.clusters = clusters;
        this.outliers = outliers;
    }

    public Map<String, Set<String>> getClusters() {
        return clusters;
    }

    public void setClusters(Map<String, Set<String>> clusters) {
        this.clusters = clusters;
    }

    public Set<String> getOutliers() {
        return outliers;
    }

    public void setOutliers(Set<String> outliers) {
        this.outliers = outliers;
    }
}

class MSP {
    private int i;
    private int j;

    public MSP() {
    }

    public MSP(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }
}

public class HCNN {
    private Graph graph;
    private List<Integer> indexes;
    private List<Vertex> G;
    private int n;
    private Map<String, Set<String>> knn;
    private Map<String, Set<String>> rkkn;

    public HCNN() {
    }

    public HCNN(Graph graph, int n, Map<String, Set<String>> knn, Map<String, Set<String>> rknn) {
        this.graph = graph;
        this.n = n;
        this.knn = knn;
        this.rkkn = rknn;
        this.indexes = new ArrayList<>();
        for(int i=0; i< this.graph.getVertices().size(); i++){
            this.indexes.add(i);
        }
        this.G = new ArrayList<>(this.graph.getVertices().keySet());
    }

    public Map<String, Set<String>> fit() throws Exception {



        ClustersAndOutliers clustersAndOutliers = this.initializeClustering();
        Map<String, Set<String>> C = clustersAndOutliers.getClusters();
        Set<String> D = clustersAndOutliers.getOutliers();

        while (C.size() > this.n) {
            int L = C.size();
            // line 4
            int tmax = Integer.MIN_VALUE;
            for (int i = 0; i <= L; i++) {
                for (int j = i + 1; j <= L; j++) {
                    int similarity = sim(C.get(Integer.toString(i)), C.get(Integer.toString(j)));
                    if (tmax < similarity) {
                        tmax = similarity;
                    }
                }
            }
            // line 5
            if (tmax == 0) {
                break;
            }

            // line 7
            Set<MSP> msp = new LinkedHashSet<>();
            for (int i = 0; i < L; i++) {
                for (int j = i + 1; j < L; j++) {
                    int similarity = sim(C.get(Integer.toString(i)), C.get(Integer.toString(j)));
                    if (similarity == tmax) {
                        msp.add(new MSP(i, j));
                    }
                }
            }
            // line 8-10
            DisjointSets DS = new DisjointSets();
            for (int i = 1; i <= L; i++) {
                DS.makeSet(C.get(Integer.toString(i)));
            }
            //line 11-12
            for (MSP msp1 : msp) {
                DS.union(C.get(Integer.toString(msp1.getI())), C.get(Integer.toString(msp1.getJ())));
            }
            // line 13-16
            for (int i = 1; i <= L; i++) {
                int index = DS.findSet(C.get(Integer.toString(i)));
                Set<String> repr = DS.getRepresentative(index).getHead().getData(); // TODO refactor
                if (repr != C.get(Integer.toString(i))) {
                    repr.addAll(C.get(Integer.toString(i)));
                    C.replace(Integer.toString(i), Collections.emptySet());
                }
            }

            // line 17-20
            Map<String, Set<String>> Ctemp = new HashMap<>(C);
            C = new HashMap<>();
            for (String cIndex : Ctemp.keySet()) {
                Set<String> c = Ctemp.get(cIndex);
                if (!c.isEmpty()) {
                    C.get(cIndex).addAll(c); // TODO this is wrong
                }
            }
        }
        C = assignOutliers(C,D);

        return C;
    }

    // TODO types will probably have to change.
    private Map<String, Set<String>> assignOutliers(Map<String, Set<String>> clusters, Set<String> outliers) {
        Map<String, Integer> label = new HashMap<>();

        for (int i = 1; i <= clusters.size(); i++) {
            for (String j : clusters.get(Integer.toString(i))) {
                label.put(j, i);
            }
        }
        for (String o : outliers) {
            int t = Integer.parseInt(o);
            if (!this.knn.get(o).isEmpty()) {
                int minStractSim = Integer.MAX_VALUE;
                for (String i : this.knn.get(o)) {
                    int stractSim = z(Integer.parseInt(o), Integer.parseInt(i));
                    if (minStractSim > stractSim) {
                        minStractSim = stractSim;
                    }
                }
                t = minStractSim;
            }
            if (label.get(Integer.toString(t)) == 0) {
                int minDist = Integer.MAX_VALUE;
                for (int i : indexes) {
                    if (label.get(Integer.toString(i)) > 0) {
                        int distance = this.dist(i, Integer.parseInt(o));
                        if (minDist > distance) {
                            minDist = distance;
                        }
                    }
                }
                t = minDist;
            }
            label.replace(o, label.get(Integer.toString(t)));
            clusters.get(Integer.toString(label.get(o))).add(o);
        }
        return clusters;
    }

    //TODO this will have to return 2 Sets, implement custom class to return 2 sets.
    private ClustersAndOutliers initializeClustering() {
        // initializing
        int nOfIndexes = this.indexes.size();
        CorePair[] corePairs = new CorePair[nOfIndexes];
        int[] label = new int[nOfIndexes];
        for (int i = 0; i < n; i++) {
            corePairs[i] = new CorePair(0, 0);
            label[i] = 0;
        }
        int last = 0;
        Set<String> outliers = new LinkedHashSet<>();
        int[][] structSimZ = new int[nOfIndexes][nOfIndexes];

        // lines 2-7
        for (int i = 1; i <= nOfIndexes; i++) {
            Set<String> Q = this.knn.get(Integer.toString(i));
            Set<String> tmp = new HashSet<>(this.knn.get(Integer.toString(i)));
            for (String j : tmp) {
                Q.addAll(this.rkkn.get(j));
            }
            for (String q : Q) {
                structSimZ[i - 1][Integer.parseInt(q) - 1] = z(i, Integer.parseInt(q));
            }
        }
        //line 8
        List<PairSim> pairSims = new ArrayList<>();
        for (int i = 1; i <= nOfIndexes; i++) {
            for (int j = i + 1; j <= nOfIndexes; j++) {
                int structSim = z(i, j);
                if (structSim > 0) {
                    pairSims.add(new PairSim(i, j, structSim));
                }
            }
        }
        // sort in descending order according to the
        //similarity between data
        //TODO this probably does not work, if it does need fixing.
        Collections.sort(pairSims, Comparator.comparing(PairSim::getSimilarity));
        Collections.reverse(pairSims);

        // lines 10 to 22
        for (int m = 0; m < pairSims.size(); m++) {
            PairSim pairSim = pairSims.get(m);
            int i = pairSim.getI();
            int j = pairSim.getJ();
            if (label[i] == 0 && label[j] == 0) {
                last += 1;
                label[i] = label[j] = last;// Not sure about that
                corePairs[last] = new CorePair(i, j);

                Set<String> union = new LinkedHashSet<>(knn.get(Integer.toString(i)));
                union.addAll(this.knn.get(Integer.toString(j)));
                for (String u : union) {
                    int ui = Integer.parseInt(u) - 1;
                    if (label[ui] == 0) {
                        label[ui] = last;
                    }
                    if (label[ui] != last) {
                        CorePair corePair = corePairs[label[ui]];
                        int h = corePair.getI();
                        int k = corePair.getJ();

                        int max = z(ui, i);
                        int temp = z(ui, j);
                        if (max < temp) {
                            max = temp;
                        }
                        int min = z(ui, h);
                        temp = z(ui, k);
                        if (min > temp) {
                            min = temp;
                        }

                        if (max > min) {
                            label[ui] = max;
                        }
                    }
                }
            }
        }
        //line 23-28
        Map<String, Set<String>> clusters = new HashMap<>();
        for (int index : this.indexes) {
            int i = index - 1;
            clusters.putIfAbsent(Integer.toString(i), new LinkedHashSet<>());
        }
        for (int index : this.indexes) {
            int i = index - 1;
            if (label[i] > 0) {
                clusters.get(Integer.toString(label[i])).add(Integer.toString(i));
            }
            if (label[i] == 0) {
                outliers.add(Integer.toString(i));
            }
        }

        return new ClustersAndOutliers(clusters, outliers);

    }

    private int z(int i, int j) {
        Set<String> intersection = new LinkedHashSet<>(this.knn.get(Integer.toString(i)));
        intersection.retainAll(this.knn.get(Integer.toString(j)));

        Set<String> union = new LinkedHashSet<>(this.knn.get(Integer.toString(i)));
        union.addAll(this.knn.get(Integer.toString(j)));

        return intersection.size() / union.size();

    }

    // TODO types will probably have to chage.
    private int conn(Set<String> clusterA, Set<String> clusterB) {
        int connSum = 0;
        for (String a : clusterA) {
            Set<String> intersection = new LinkedHashSet<>(this.knn.get(a));
            intersection.retainAll(clusterB);
            connSum += intersection.size();
        }

        for (String b : clusterB) {
            Set<String> intersection = new LinkedHashSet<>(this.knn.get(b));
            intersection.retainAll(clusterA);
            connSum += intersection.size();
        }
        return connSum;
    }

    // TODO types will probably have to chage.
    private int link(Set<String> clusterA, Set<String> clusterB) {
        Set<Set<String>> linkSet = new LinkedHashSet<>();
        for (String a : clusterA) {
            Set<String> intersection = new LinkedHashSet<>(this.knn.get(a));
            intersection.retainAll(clusterB);
            if (!intersection.isEmpty()) {
                linkSet.add(intersection);
            }
        }
        return linkSet.size();
    }

    // TODO types will probably have to chage.
    private int sim(Set<String> clusterA, Set<String> clusterB) {
        return (conn(clusterA, clusterB) / (clusterA.size() * clusterB.size())) * (link(clusterA, clusterB) / clusterA.size()) * (link(clusterB, clusterA) / clusterB.size());
    }

    // TODO this is calculating distances for all the vertices compared to source, refactor to find dist of source to destination vertex.
    public int dist(int source, int dest) {
        int nOfVertices = this.graph.getVertices().size() + 1;
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
        while (!minHeap.isEmpty()) {
            // Remove and return the best vertex
            Vertex vertex = minHeap.poll();

            // get the vertex number
            int u = Integer.parseInt(vertex.getLabel());

            // do for each neighbor `v` of `u`
            for (Vertex edge : this.graph.getEdges(Integer.toString(u))) {
                int v = Integer.parseInt(edge.getLabel());
                int weight = 1;

                // Relaxation step
                if (!done[v] && (dist.get(u) + weight) < dist.get(v)) {
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
