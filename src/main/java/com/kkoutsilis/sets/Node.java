package com.kkoutsilis.sets;

import com.kkoutsilis.graphs.Vertex;

import java.util.Set;

public class Node {
    private Set<Vertex> data;
    private Node next;
    private Representative representative;

    public Node(Set<Vertex> data) {
        this.next = null;
        this.representative = null;
        this.data = data;
    }

    public Set<Vertex> getData() {
        return data;
    }

    public void setData(Set<Vertex> data) {
        this.data = data;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    @Override
    public String toString() {
        return "Node{" + "data=" + data + ", next=" + next + '}';
    }
}
