package com.kkoutsilis.algorithms;

import com.kkoutsilis.algorithms.helpers.ClustersAndOutliers;
import com.kkoutsilis.algorithms.helpers.CorePair;
import com.kkoutsilis.algorithms.helpers.MSP;
import com.kkoutsilis.algorithms.helpers.PairSim;
import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;
import com.kkoutsilis.sets.DisjointSets;
import com.kkoutsilis.utilities.Distance;

import java.util.*;

/**
 * @author kkoutsilis
 */
public class HCNN implements ClusteringAlgorithm {
    private Graph graph;
    private List<Integer> indexes;
    private List<Vertex> G;
    private int n;
    private Map<Vertex, Set<Vertex>> fistNnAlgorithm;
    private Map<Vertex, Set<Vertex>> secondNnAlgorithm;

    public HCNN() {
    }

    public HCNN(Graph graph, int n, Map<Vertex, Set<Vertex>> fistNnAlgorithm,
            Map<Vertex, Set<Vertex>> secondNnAlgorithm) {
        this.graph = graph;
        this.n = n;
        this.fistNnAlgorithm = fistNnAlgorithm;
        this.secondNnAlgorithm = secondNnAlgorithm;
        this.indexes = new ArrayList<>();
        for (int i = 0; i < this.graph.getVertices().size(); i++) {
            this.indexes.add(i);
        }
        this.G = new ArrayList<>(this.graph.getVertices().keySet());
    }

    @Override
    public List<Set<Vertex>> fit() throws Exception {
        ClustersAndOutliers clustersAndOutliers = this.initializeClustering();
        List<Set<Vertex>> C = clustersAndOutliers.getClusters();
        Set<Vertex> D = clustersAndOutliers.getOutliers();

        while (C.size() > this.n) {
            int L = C.size();
            float tmax = 0;
            for (int i = 0; i < L; i++) {
                for (int j = i + 1; j < L; j++) {
                    float similarity = sim(C.get(i), C.get(j));
                    if (tmax < similarity) {
                        tmax = similarity;
                    }
                }
            }
            if (tmax == 0) {
                break;
            }
            Set<MSP> msp = new LinkedHashSet<>();
            for (int i = 0; i < L; i++) {
                for (int j = i + 1; j < L; j++) {
                    float similarity = sim(C.get(i), C.get(j));
                    if (similarity == tmax) {
                        msp.add(new MSP(i, j));
                    }
                }
            }
            DisjointSets DS = new DisjointSets();
            for (Set<Vertex> vertices : C) {
                DS.makeSet(vertices);
            }
            for (MSP msp1 : msp) {
                int i = msp1.getI();
                int j = msp1.getJ();
                DS.union(C.get(i), C.get(j));
            }
            for (int i = 0; i < L; i++) {
                int index = DS.findSet(C.get(i));
                Set<Vertex> repr = DS.getRepresentative(index).getHead().getData();
                if (repr != C.get(i)) {
                    repr.addAll(C.get(i));
                    C.set(i, Collections.emptySet());
                }
            }
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

    private List<Set<Vertex>> assignOutliers(List<Set<Vertex>> clusters, Set<Vertex> outliers) {
        float[] label = new float[this.indexes.size() + 1];
        Set<Vertex> loners = new HashSet<>();
        for (int i = 0; i <= this.indexes.size(); i++) {
            label[i] = 0;
        }
        for (int i = 0; i < clusters.size(); i++) {
            for (Vertex j : clusters.get(i)) {
                label[j.getLabel()] = i;
            }
        }
        for (Vertex o : outliers) {
            float t = o.getLabel();
            if (!this.fistNnAlgorithm.get(o).isEmpty()) {
                float minStractSim = Float.MAX_VALUE;
                for (Vertex i : this.fistNnAlgorithm.get(o)) {
                    float stractSim = z(o, i);
                    if (minStractSim > stractSim) {
                        minStractSim = stractSim;
                    }
                }
                t = minStractSim;
            }
            if (label[(int) t] == 0) {
                int minDist = Integer.MAX_VALUE;
                for (int i : indexes) {
                    if (label[i] > 0) {
                        int distance = Distance.calculate(i, o.getLabel(), this.graph);
                        if (minDist > distance) {
                            minDist = distance;
                        }
                    }
                }
                t = minDist;
            }

            if (t == Integer.MAX_VALUE) {
                loners.add(o);
            } else {
                label[o.getLabel()] = label[(int) t];
                clusters.get((int) label[o.getLabel()]).add(o);
            }
        }
        clusters.add(loners);
        clusters.removeIf(Set::isEmpty);
        return clusters;
    }

    private ClustersAndOutliers initializeClustering() {
        int nOfIndexes = this.indexes.size();
        CorePair[] corePairs = new CorePair[nOfIndexes];
        int[] label = new int[nOfIndexes];
        for (int i = 0; i < nOfIndexes; i++) {
            corePairs[i] = new CorePair(0, 0);
            label[i] = 0;
        }
        int last = 0;
        Set<Vertex> outliers = new LinkedHashSet<>();
        float[][] structSimZ = new float[nOfIndexes][nOfIndexes];

        for (Vertex i : this.G) {
            Set<Vertex> Q = this.fistNnAlgorithm.get(i);
            Set<Vertex> tmp = new HashSet<>(this.fistNnAlgorithm.get(i));
            for (Vertex j : tmp) {
                Q.addAll(this.secondNnAlgorithm.get(j));
            }
            for (Vertex q : Q) {
                structSimZ[i.getLabel() - 1][q.getLabel() - 1] = z(i, q);
            }
        }

        List<PairSim> pairSims = new ArrayList<>();
        for (int i = 0; i < nOfIndexes; i++) {
            for (int j = i + 1; j < nOfIndexes; j++) {
                Vertex vI = this.G.get(i);
                Vertex vJ = this.G.get(j);
                float structSim = z(vI, vJ);
                if (structSim > 0) {
                    pairSims.add(new PairSim(vI.getLabel(), vJ.getLabel(), structSim));
                }
            }
        }
        // sort in descending order according to the similarity between data
        pairSims.sort(Comparator.comparing(PairSim::getSimilarity));
        Collections.reverse(pairSims);

        for (PairSim pairSim : pairSims) {
            int i = pairSim.getI() - 1;
            int j = pairSim.getJ() - 1;
            if (label[i] == 0 && label[j] == 0) {
                last += 1;
                label[i] = label[j] = last;
                corePairs[last] = new CorePair(i, j);
                Vertex vI = this.G.get(i);
                Vertex vJ = this.G.get(j);
                Set<Vertex> union = new LinkedHashSet<>(fistNnAlgorithm.get(vI));
                union.addAll(this.fistNnAlgorithm.get(vJ));
                for (Vertex u : union) {
                    int ui = this.G.indexOf(u);
                    if (label[ui] == 0) {
                        label[ui] = last;
                    }
                    if (label[ui] != last) {
                        CorePair corePair = corePairs[label[ui]];
                        int h = corePair.getI();
                        int k = corePair.getJ();

                        Vertex vuI = this.G.get(ui);
                        float max = z(vuI, vI);
                        float temp = z(vuI, vJ);
                        if (max < temp) {
                            max = temp;
                        }
                        Vertex vH = this.G.get(h);
                        Vertex vK = this.G.get(k);

                        float min = z(vuI, vH);
                        temp = z(vuI, vK);
                        if (min > temp) {
                            min = temp;
                        }

                        if (max > min) {
                            label[ui] = last;
                        }
                    }
                }
            }
        }

        List<Set<Vertex>> clusters = new ArrayList<>();
        for (int i = 0; i <= this.indexes.size(); i++) {
            clusters.add(new HashSet<>());
        }
        for (int i : this.indexes) {
            Vertex vI = this.G.get(i);
            if (label[i] > 0) {
                clusters.get(label[i]).add(vI);
            }
            if (label[i] == 0) {
                outliers.add(vI);
            }
        }
        clusters.removeIf(Set::isEmpty); // Not the best but works for now.
        return new ClustersAndOutliers(clusters, outliers);

    }

    private float z(Vertex i, Vertex j) {
        Set<Vertex> intersection = new LinkedHashSet<>(this.fistNnAlgorithm.get(i));
        intersection.retainAll(this.fistNnAlgorithm.get(j));

        Set<Vertex> union = new LinkedHashSet<>(this.fistNnAlgorithm.get(i));
        union.addAll(this.fistNnAlgorithm.get(j));

        return (float) intersection.size() / (float) union.size();
    }

    private int conn(Set<Vertex> clusterA, Set<Vertex> clusterB) {
        int connSum = 0;
        for (Vertex a : clusterA) {
            Set<Vertex> intersection = new LinkedHashSet<>(this.fistNnAlgorithm.get(a));
            intersection.retainAll(clusterB);
            connSum += intersection.size();
        }

        for (Vertex b : clusterB) {
            Set<Vertex> intersection = new LinkedHashSet<>(this.fistNnAlgorithm.get(b));
            intersection.retainAll(clusterA);
            connSum += intersection.size();
        }
        return connSum;
    }

    private int link(Set<Vertex> clusterA, Set<Vertex> clusterB) {
        Set<Set<Vertex>> linkSet = new LinkedHashSet<>();
        for (Vertex a : clusterA) {
            Set<Vertex> intersection = new LinkedHashSet<>(this.fistNnAlgorithm.get(a));
            intersection.retainAll(clusterB);
            if (!intersection.isEmpty()) {
                linkSet.add(intersection);
            }
        }
        return linkSet.size();
    }

    private float sim(Set<Vertex> clusterA, Set<Vertex> clusterB) {
        return ((float) conn(clusterA, clusterB) / (float) (clusterA.size() * clusterB.size()))
                * ((float) link(clusterA, clusterB) / (float) clusterA.size())
                * ((float) link(clusterB, clusterA) / (float) clusterB.size());
    }
}
