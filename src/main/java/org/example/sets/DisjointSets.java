package org.example.sets;


import java.util.ArrayList;

public class DisjointSets {
    private static final ArrayList<Representative> sets = new ArrayList<>();

    //RETURN INDEX?
    public static Representative makeSet(int value) throws Exception {
        if (findSet(value) != -1) {
            throw new Exception("Value " + value + " already exists in set");
        }
        Node node = new Node(value);
        Representative representative = new Representative();
        node.setRepresentative(representative);
        representative.setHead(node);
        representative.setTail(node);
        sets.add(representative);
        return representative;
    }

    public static int findSet(int value) {
        for (int i = 0; i < sets.size(); i++) {
            Node searchNode = sets.get(i).getHead();
            while (searchNode != null) {
                if (searchNode.getData() == value) {
                    return i;
                }
                searchNode = searchNode.getNext();
            }
        }
        return -1;
    }

    // RETURN INDEX
    public static Representative union(int valueX, int valueY) throws Exception {
        int indexX = findSet(valueX);
        int indexY = findSet(valueY);
        if (indexX == -1 || indexY == -1) {
            throw new Exception("Value does not exist in set, cannot union");
        }
        Representative repX = sets.get(indexX);
        Representative repY = sets.get(indexY);
        Node tailX = repX.getTail();
        tailX.setNext(repY.getHead());
        repX.setTail(repY.getTail());
        Node nodeY = repY.getHead();
        do {
            nodeY.setRepresentative(repX);
            nodeY = nodeY.getNext();
        } while (nodeY != null);

        sets.remove(indexY);
        return repX;
    }

    public static void printSets() {
        for (int i = 0; i < sets.size(); i++) {
            Representative rep = sets.get(i);
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
