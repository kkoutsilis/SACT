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
    private List<Set<Vertex>> clusters;
    private Set<Vertex> outliers;

    public ClustersAndOutliers() {
    }

    public ClustersAndOutliers(List<Set<Vertex>> clusters, Set<Vertex> outliers) {
        this.clusters = clusters;
        this.outliers = outliers;
    }

    public List<Set<Vertex>> getClusters() {
        return clusters;
    }

    public void setClusters(List<Set<Vertex>> clusters) {
        this.clusters = clusters;
    }

    public Set<Vertex> getOutliers() {
        return outliers;
    }

    public void setOutliers(Set<Vertex> outliers) {
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
    private Map<Vertex, Set<Vertex>> knn;
    private Map<Vertex, Set<Vertex>> rkkn;

    public HCNN() {
    }

    public HCNN(Graph graph, int n, Map<Vertex, Set<Vertex>> knn, Map<Vertex, Set<Vertex>> rknn) {
        this.graph = graph;
        this.n = n;
        this.knn = knn;
        this.rkkn = rknn;
        this.indexes = new ArrayList<>();
        for (int i = 0; i < this.graph.getVertices().size(); i++) {
            this.indexes.add(i);
        }
        this.G = new ArrayList<>(this.graph.getVertices().keySet());
    }

    public List<Set<Vertex>> fit() throws Exception {

        ClustersAndOutliers clustersAndOutliers = this.initializeClustering();
        List<Set<Vertex>> C = clustersAndOutliers.getClusters();
        Set<Vertex> D = clustersAndOutliers.getOutliers();

        while (C.size() > this.n) {
            int L = C.size();
            // line 4
            int tmax = Integer.MIN_VALUE;
            for (int i = 0; i <= L; i++) {
                for (int j = i + 1; j <= L; j++) {
                    int similarity = sim(C.get(i), C.get(j));
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
                    int similarity = sim(C.get(i), C.get(j));
                    if (similarity == tmax) {
                        msp.add(new MSP(i, j));
                    }
                }
            }
            // line 8-10
            DisjointSets DS = new DisjointSets();
            for (int i = 1; i <= L; i++) {
                DS.makeSet(C.get(i));
            }
            //line 11-12
            for (MSP msp1 : msp) {
                DS.union(C.get(msp1.getI()), C.get(msp1.getJ()));
            }
            // line 13-16
            for (int i = 1; i <= L; i++) {
                int index = DS.findSet(C.get(i));
                Set<Vertex> repr = DS.getRepresentative(index).getHead().getData(); // TODO refactor
                if (repr != C.get(i)) {
                    repr.addAll(C.get(i));
                    C.set(i, Collections.emptySet());
                }
            }

            // line 17-20
            List<Set<Vertex>> Ctemp = new ArrayList<>(C);
            C = new ArrayList<>();
            for (Set<Vertex> c : Ctemp) {
                if (!c.isEmpty()) {
                    C.add(c);
                }
            }
        }
        C = assignOutliers(C, D);

        return C;
    }

    // TODO types will probably have to change.
    private List<Set<Vertex>> assignOutliers(List<Set<Vertex>> clusters, Set<Vertex> outliers) {
        int[] label = new int[clusters.size()];
        for (int i = 0; i < clusters.size(); i++) {
            for (Vertex j : clusters.get(i)) {
                label[j.getLabel()] = i;
            }
        }
        for (Vertex o : outliers) {
            int t = o.getLabel(); // wrong
            if (!this.knn.get(o).isEmpty()) {
                int minStractSim = Integer.MAX_VALUE;
                for (Vertex i : this.knn.get(o)) {
                    int stractSim = z(o, i);
                    if (minStractSim > stractSim) {
                        minStractSim = stractSim;
                    }
                }
                t = minStractSim;
            }
            if (label[t] == 0) {
                int minDist = Integer.MAX_VALUE;
                for (int i : indexes) {
                    if (label[i] > 0) {
                        int distance = this.dist(i, o.getLabel());
                        if (minDist > distance) {
                            minDist = distance;
                        }
                    }
                }
                t = minDist;
            }
            label[o.getLabel()] = t;
            clusters.get(label[o.getLabel()]).add(o);
        }
        return clusters;
    }

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
        Set<Vertex> outliers = new LinkedHashSet<>();
        int[][] structSimZ = new int[nOfIndexes+1][nOfIndexes+1];

        // lines 2-7
        for (Vertex i : this.G) {
            Set<Vertex> Q = this.knn.get(i);
            Set<Vertex> tmp = new HashSet<>(this.knn.get(i));
            for (Vertex j : tmp) {
                Q.addAll(this.rkkn.get(j));
            }
            for (Vertex q : Q) {
                structSimZ[i.getLabel() ][q.getLabel() ] = z(i, q);
            }
        }

        //line 8
        List<PairSim> pairSims = new ArrayList<>();
        for (int i = 0; i < nOfIndexes; i++) {
            for (int j = i + 1; j < nOfIndexes; j++) {
                Vertex vI = this.G.get(i);
                Vertex vJ = this.G.get(j);
                int structSim = z(vI, vJ);
                if (structSim > 0) {
                    pairSims.add(new PairSim(i, j, structSim));
                }
            }
        }
        // sort in descending order according to the similarity between data
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
                label[i] = label[j] = last;// Not sure about that, line 14
                corePairs[last] = new CorePair(i, j);
                Vertex vI = this.G.get(i);
                Vertex vJ = this.G.get(j);
                Set<Vertex> union = new LinkedHashSet<>(knn.get(vI));
                union.addAll(this.knn.get(vJ));
                for (Vertex u : union) {
                    int ui = this.G.indexOf(u); // wrong ??
                    if (label[ui] == 0) {
                        label[ui] = last;
                    }
                    if (label[ui] != last) {
                        CorePair corePair = corePairs[label[ui]];
                        int h = corePair.getI();
                        int k = corePair.getJ();

                        Vertex vuI = this.G.get(ui);
                        int max = z(vuI, vI);
                        int temp = z(vuI, vJ);
                        if (max < temp) {
                            max = temp;
                        }
                        Vertex vH = this.G.get(h);
                        Vertex vK = this.G.get(k);

                        int min = z(vuI, vH);
                        temp = z(vuI, vK);
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
        List<Set<Vertex>> clusters = new ArrayList<>();
        for (int i : this.indexes) {
            clusters.add(new HashSet<>());
        }
        for (int i : this.indexes) {
            Vertex vlabelI = this.G.get(label[i]);
            if (label[i] > 0) {
                clusters.get(label[i]).add(vlabelI);
            }
            if (label[i] == 0) {
                outliers.add(vlabelI);
            }
        }
        return new ClustersAndOutliers(clusters, outliers);

    }

    private int z(Vertex i, Vertex j) {
        Set<Vertex> intersection = new LinkedHashSet<>(this.knn.get(i));
        intersection.retainAll(this.knn.get(j));

        Set<Vertex> union = new LinkedHashSet<>(this.knn.get(i));
        union.addAll(this.knn.get(j));

        return intersection.size() / union.size();

    }

    // TODO types will probably have to chage.
    private int conn(Set<Vertex> clusterA, Set<Vertex> clusterB) {
        int connSum = 0;
        for (Vertex a : clusterA) {
            Set<Vertex> intersection = new LinkedHashSet<>(this.knn.get(a));
            intersection.retainAll(clusterB);
            connSum += intersection.size();
        }

        for (Vertex b : clusterB) {
            Set<Vertex> intersection = new LinkedHashSet<>(this.knn.get(b));
            intersection.retainAll(clusterA);
            connSum += intersection.size();
        }
        return connSum;
    }

    // TODO types will probably have to chage.
    private int link(Set<Vertex> clusterA, Set<Vertex> clusterB) {
        Set<Set<Vertex>> linkSet = new LinkedHashSet<>();
        for (Vertex a : clusterA) {
            Set<Vertex> intersection = new LinkedHashSet<>(this.knn.get(a));
            intersection.retainAll(clusterB);
            if (!intersection.isEmpty()) {
                linkSet.add(intersection);
            }
        }
        return linkSet.size();
    }

    // TODO types will probably have to chage.
    private int sim(Set<Vertex> clusterA, Set<Vertex> clusterB) {
        return (conn(clusterA, clusterB) / (clusterA.size() * clusterB.size())) * (link(clusterA, clusterB) / clusterA.size()) * (link(clusterB, clusterA) / clusterB.size());
    }

    public int dist(int source, int dest) {
        int nOfVertices = this.graph.getVertices().size() + 1;
        // create a min-heap and push source node having distance 0
        PriorityQueue<Vertex> minHeap;
        minHeap = new PriorityQueue<>(Comparator.comparingInt(Vertex::getLabel));
        minHeap.add(new Vertex(source));

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
            int u = vertex.getLabel();

            // do for each neighbor `v` of `u`
            for (Vertex edge : graph.getEdges(u)) {
                int v = edge.getLabel();
                int weight = 1;

                // Relaxation step
                if (!done[v] && (dist.get(u) + weight) < dist.get(v)) {
                    dist.set(v, dist.get(u) + weight);
                    prev[v] = u;
                    minHeap.add(new Vertex(v));
                }
            }
            // mark vertex `u` as done so it will not get picked up again
            done[u] = true;
        }
        return dist.get(dest);
    }
}
