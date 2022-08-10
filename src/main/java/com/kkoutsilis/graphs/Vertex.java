package com.kkoutsilis.graphs;

import java.util.Objects;

public class Vertex {
    private int label;
    private String schema;
    private String type;

    public Vertex() {
    }

    public Vertex(int label, String schema, String type) {
        this.label = label;
        this.schema = schema;
        this.type = type;
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

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Vertex{" + "label=" + label + ", schema='" + schema + '\'' + ", type='" + type + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex vertex = (Vertex) o;
        return getLabel() == vertex.getLabel() && Objects.equals(getSchema(), vertex.getSchema()) && Objects.equals(getType(), vertex.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLabel());
    }
}
