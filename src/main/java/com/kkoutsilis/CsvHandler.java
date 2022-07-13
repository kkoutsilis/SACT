package com.kkoutsilis;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class CsvHandler {
    private CsvHandler() {
        throw new IllegalStateException("Utility class");
    }
    public static Graph parseCSV(String path) throws IllegalStateException {
        Graph graph = new Graph();
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(path)).withSkipLines(1)           // skip the first line, header info
                .build()) {
            List<String[]> r = reader.readAll();
            for (String[] row : r) {
                Vertex v1 = new Vertex(Integer.parseInt(row[2]), row[1], row[3]);
                Vertex v2 = new Vertex(Integer.parseInt(row[5]), row[4], row[6]);
                graph.addVertex(v1);
                graph.addVertex(v2);
                graph.addEdge(v1, v2);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return graph;
    }

    public static void dumpToCSV(String path, List<Set<Vertex>> data) {
        File file = new File(path);
        try {
            FileWriter outfile = new FileWriter(file);

            CSVWriter writer = new CSVWriter(outfile);

            String[] header = {"CLUSTER_ID", "SCHEMA", "NAME", "TYPE"};
            writer.writeNext(header);

            int clusterID = 0;
            for (Set<Vertex> vertexSet : data) {
                for (Vertex vertex : vertexSet) {
                    String[] output = {Integer.toString(clusterID), vertex.getSchema(), Integer.toString(vertex.getLabel()), vertex.getType()};
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
