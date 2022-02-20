package org.example.distance;

import java.util.Map;

public class ManhattanDistance implements Distance {
    @Override
    public double calculate(Map<String, Double> f1, Map<String, Double> f2) {
        double sum = 0;
        for (String key : f1.keySet()) {
            Double v1 = f1.get(key);
            Double v2 = f2.get(key);
            if (v1 != null && v2 != null) {
                sum += Math.abs(v1 - v2);
            }
        }
        return sum;
    }
}
