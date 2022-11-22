package com.kkoutsilis.utilities;

import com.kkoutsilis.graphs.Vertex;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public final class CsvHandler {
    private CsvHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static Map<Vertex, Set<Vertex>> parseCSV(String path) throws IllegalStateException {
        Map<Vertex, Set<Vertex>> vertices = new HashMap<>();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withSkipLines(1).build()) {
            List<String[]> r = reader.readAll();
            for (String[] row : r) {
                Vertex v1 = new Vertex(Integer.parseInt(row[2]), row[1], row[3]);
                Vertex v2 = new Vertex(Integer.parseInt(row[5]), row[4], row[6]);
                vertices.putIfAbsent(v1, new LinkedHashSet<>());
                vertices.putIfAbsent(v2, new LinkedHashSet<>());
                vertices.get(v1).add(v2);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return vertices;
    }

    public static void dumpToCSV(String path, List<Set<Vertex>> data) {
        File file = new File(path);
        try {
            FileWriter outfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outfile);

            String[] header = { "CLUSTER_ID", "SCHEMA", "NAME", "TYPE" };
            writer.writeNext(header);

            int clusterID = 0;
            for (Set<Vertex> vertexSet : data) {
                for (Vertex vertex : vertexSet) {
                    String[] output = { Integer.toString(clusterID), vertex.getSchema(),
                            Integer.toString(vertex.getLabel()), vertex.getType() };
                    writer.writeNext(output);
                }
                clusterID++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
