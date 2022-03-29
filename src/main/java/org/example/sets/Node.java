package org.example.sets;

import java.util.Set;

public class Node {
    private Set<String> data;
    private Node next;
    private Representative representative;

    public Node(Set<String> data) {
        this.next = null;
        this.representative = null;
        this.data = data;
    }

    public Set<String> getData() {
        return data;
    }

    public void setData(Set<String> data) {
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
        return "Node{" +
                "data=" + data +
                ", next=" + next +
                '}';
    }
}
