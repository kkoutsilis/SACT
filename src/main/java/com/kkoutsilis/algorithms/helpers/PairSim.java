package com.kkoutsilis.algorithms.helpers;

import java.util.Objects;

/**
 * @author kkoutsilis
 */
public class PairSim {
    private int i;
    private int j;
    private float similarity;

    public PairSim() {
    }

    public PairSim(int i, int j, float similarity) {
        this.i = i;
        this.j = j;
        this.similarity = similarity;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public float getSimilarity() {
        return similarity;
    }

    public void setSimilarity(float similarity) {
        this.similarity = similarity;
    }

    public void setPairSim(int i, int j, float similarity) {
        this.setI(i);
        this.setJ(j);
        this.setSimilarity(similarity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PairSim))
            return false;
        PairSim pairSim = (PairSim) o;
        return getI() == pairSim.getI() && getJ() == pairSim.getJ() && getSimilarity() == pairSim.getSimilarity();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getI(), getJ(), getSimilarity());
    }

    @Override
    public String toString() {
        return "PairSim{" + "i=" + i + ", j=" + j + ", similarity=" + similarity + '}';
    }
}
