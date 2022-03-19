package org.example;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.math3.stat.inference.BinomialTest;

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
    // types are probably wrong
    private Set<String> assignOutliers(Map<String,Set<String>> clusters, Set<Integer> indexes, Set<Integer> outliers ){
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

            }

        }
        return Collections.emptySet();
    }
     // this will have to return 2 Sets
    private Set<String> initializeClustering(){
        return Collections.emptySet();
    }

    //probably wrong
    private int z(int i, int j){
        Set<String> intersection = new LinkedHashSet<>(this.knn.get(Integer.toString(i)));
        intersection.retainAll(this.knn.get(Integer.toString(j)));

        Set<String> union = new LinkedHashSet<>(knn.get(i));
        union.addAll(this.knn.get(Integer.toString(j)));

        return intersection.size()/union.size();

    }
}
