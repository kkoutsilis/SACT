package com.kkoutsilis.sets;

/**
 * @author kkoutsilis
 */
public class Representative {
    private Node head;
    private Node tail;

    public Representative() {
        this.head = null;
        this.tail = null;
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    @Override
    public String toString() {
        return "Representative{" +
                "head=" + head +
                ", tail=" + tail +
                '}';
    }
}
