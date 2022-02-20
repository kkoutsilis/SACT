package org.example;

import org.example.sets.DisjointSets;
import org.example.sets.Representative;

public class App {
    public static void main(String[] args) throws Exception {
        Representative rep1 = DisjointSets.makeSet(1);
        Representative rep2 = DisjointSets.makeSet(2);
        DisjointSets.union(1, 2);
        Representative rep3 = DisjointSets.makeSet(3);
        Representative rep4 = DisjointSets.makeSet(4);
        DisjointSets.union(3, 4);
        Representative rep5 = DisjointSets.makeSet(5);
        Representative rep6 = DisjointSets.makeSet(6);
        DisjointSets.union(5, 6);
        DisjointSets.union(1, 3);
        DisjointSets.printSets();

    }
}
