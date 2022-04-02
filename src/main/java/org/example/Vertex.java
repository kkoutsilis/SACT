package org.example;

import java.util.Objects;

public class Vertex {
    private int label;

    public Vertex() {
    }

    public Vertex(int label) {
        this.label = label;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "label='" + label + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex vertex = (Vertex) o;
        return getLabel() == vertex.getLabel();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabel());
    }
}
