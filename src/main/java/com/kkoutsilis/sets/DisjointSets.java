package com.kkoutsilis.sets;


import com.kkoutsilis.graphs.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DisjointSets {
    private List<Representative> sets;

    public DisjointSets() {
        this.sets = new ArrayList<>();
    }

    public int makeSet(Set<Vertex> value) throws Exception {
        if (this.findSet(value) != -1) {
            throw new Exception("Value " + value + " already exists in set");
        }
        Node node = new Node(value);
        Representative representative = new Representative();
        node.setRepresentative(representative);
        representative.setHead(node);
        representative.setTail(node);
        sets.add(representative);
        return findSet(representative.getHead().getData());
    }

    public int findSet(Set<Vertex> value) {
        for (int i = 0; i < this.sets.size(); i++) {
            Node searchNode = this.sets.get(i).getHead();
            while (searchNode != null) {
                if (searchNode.getData() == value) {
                    return i;
                }
                searchNode = searchNode.getNext();
            }
        }
        return -1;
    }

    public int union(Set<Vertex> valueX, Set<Vertex> valueY) throws Exception {
        int indexX = findSet(valueX);
        int indexY = findSet(valueY);
        if (indexX == -1 || indexY == -1) {
            throw new Exception("Value does not exist in set, cannot union");
        }
        Representative repX = this.sets.get(indexX);
        Representative repY = this.sets.get(indexY);
        Node tailX = repX.getTail();
        tailX.setNext(repY.getHead());
        repX.setTail(repY.getTail());
        Node nodeY = repY.getHead();
        do {
            nodeY.setRepresentative(repX);
            nodeY = nodeY.getNext();
        } while (nodeY != null);

        sets.remove(indexY);
        return indexX;
    }

    public Representative getRepresentative(int index) {
        return this.sets.get(index);
    }

    public void printSets() {
        for (int i = 0; i < this.sets.size(); i++) {
            Representative rep = this.sets.get(i);
            System.out.print("Set " + i + " : { ");
            Node node = rep.getHead();
            while (node != null) {
                System.out.print(node.getData() + ", ");
                node = node.getNext();
            }
            System.out.print("}\n");
        }
    }
}
