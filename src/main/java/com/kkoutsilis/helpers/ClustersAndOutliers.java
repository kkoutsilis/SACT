package com.kkoutsilis.helpers;


import java.util.List;
import java.util.Set;

public class ClustersAndOutliers {
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

    @Override
    public String toString() {
        return "ClustersAndOutliers{" + "clusters=" + clusters + ", outliers=" + outliers + '}';
    }
}
