package com.kkoutsilis.helpers;

import java.util.Objects;

public class CorePair {
    private int i;
    private int j;

    public CorePair() {

    }

    public CorePair(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return this.i;
    }

    public int getJ() {
        return this.j;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public void setPair(int i, int j) {
        this.setI(i);
        this.setJ(j);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CorePair)) return false;
        CorePair corePair = (CorePair) o;
        return getI() == corePair.getI() && getJ() == corePair.getJ();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getI(), getJ());
    }

    @Override
    public String toString() {
        return "CorePair{" + "i=" + i + ", j=" + j + '}';
    }
}