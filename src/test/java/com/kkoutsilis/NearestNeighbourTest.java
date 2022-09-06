package com.kkoutsilis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import com.kkoutsilis.graphs.Graph;
import com.kkoutsilis.graphs.Vertex;
import org.junit.Before;
import org.junit.Test;


import java.util.*;

import com.kkoutsilis.utilities.NearestNeighbour;
import org.unitils.reflectionassert.ReflectionComparatorMode;


/**
 * Unit test for NearestNeighbour.
 */
public class NearestNeighbourTest {
    Graph testGraph;
    List<Vertex> vertexList;

    @Before
    public void setUp() {
        testGraph = new Graph();
        vertexList = new ArrayList<>();
        for (int i =1; i<=11;i++) {
            Vertex tmp = new Vertex(i, "test", "test");
            vertexList.add(tmp);
            testGraph.addVertex(tmp);
        }
        testGraph.addEdge(vertexList.get(9),vertexList.get(0));
        testGraph.addEdge(vertexList.get(9),vertexList.get(1));
        testGraph.addEdge(vertexList.get(9),vertexList.get(5));
        testGraph.addEdge(vertexList.get(9),vertexList.get(7));

        testGraph.addEdge(vertexList.get(0),vertexList.get(1));
        testGraph.addEdge(vertexList.get(0),vertexList.get(2));
        testGraph.addEdge(vertexList.get(0),vertexList.get(3));

        testGraph.addEdge(vertexList.get(1),vertexList.get(2));
        testGraph.addEdge(vertexList.get(1),vertexList.get(3));
        testGraph.addEdge(vertexList.get(1),vertexList.get(4));

        testGraph.addEdge(vertexList.get(2),vertexList.get(3));
        testGraph.addEdge(vertexList.get(2),vertexList.get(4));

        testGraph.addEdge(vertexList.get(3),vertexList.get(4));


        testGraph.addEdge(vertexList.get(5),vertexList.get(6));
        testGraph.addEdge(vertexList.get(5),vertexList.get(7));
        testGraph.addEdge(vertexList.get(5),vertexList.get(8));
        testGraph.addEdge(vertexList.get(5),vertexList.get(10));

        testGraph.addEdge(vertexList.get(6),vertexList.get(7));
        testGraph.addEdge(vertexList.get(6),vertexList.get(8));
        testGraph.addEdge(vertexList.get(6),vertexList.get(10));

        testGraph.addEdge(vertexList.get(7),vertexList.get(8));
        testGraph.addEdge(vertexList.get(7),vertexList.get(5));

        testGraph.addEdge(vertexList.get(8),vertexList.get(10));
        testGraph.addEdge(vertexList.get(8),vertexList.get(6));
        testGraph.addEdge(vertexList.get(8),vertexList.get(7));
    }
    @Test
    public void  testKnn()
    {
        Map<Vertex, Set<Vertex>> expected = new HashMap<>() ;
        expected.put(vertexList.get(0),new HashSet<>(Arrays.asList(vertexList.get(1),vertexList.get(2),vertexList.get(3))));
        expected.put(vertexList.get(1),new HashSet<>(Arrays.asList(vertexList.get(2),vertexList.get(3),vertexList.get(4))));
        expected.put(vertexList.get(2),new HashSet<>(Arrays.asList(vertexList.get(3),vertexList.get(4))));
        expected.put(vertexList.get(3),new HashSet<>(Arrays.asList(vertexList.get(4))));
        expected.put(vertexList.get(4),new HashSet<>());
        expected.put(vertexList.get(5),new HashSet<>(Arrays.asList(vertexList.get(6),vertexList.get(7),vertexList.get(8))));
        expected.put(vertexList.get(6),new HashSet<>(Arrays.asList(vertexList.get(7),vertexList.get(8),vertexList.get(10))));
        expected.put(vertexList.get(7),new HashSet<>(Arrays.asList(vertexList.get(5),vertexList.get(8),vertexList.get(10))));
        expected.put(vertexList.get(8),new HashSet<>(Arrays.asList(vertexList.get(6),vertexList.get(7),vertexList.get(10))));
        expected.put(vertexList.get(9),new HashSet<>(Arrays.asList(vertexList.get(0),vertexList.get(1),vertexList.get(5))));
        expected.put(vertexList.get(10),new HashSet<>());

        Map<Vertex, Set<Vertex>> res = NearestNeighbour.knn(3, testGraph);
        for(Map.Entry<Vertex, Set<Vertex>> entry : res.entrySet()) {
            assertReflectionEquals(entry.getValue(), expected.get(entry.getKey()), ReflectionComparatorMode.LENIENT_ORDER);
        }
    }

    @Test
    public void  testRknn()
    {
        Map<Vertex, Set<Vertex>> expected = new HashMap<>() ;
        expected.put(vertexList.get(0),new HashSet<>(Arrays.asList(vertexList.get(9))));
        expected.put(vertexList.get(1),new HashSet<>(Arrays.asList(vertexList.get(0),vertexList.get(9))));
        expected.put(vertexList.get(2),new HashSet<>(Arrays.asList(vertexList.get(0),vertexList.get(1))));
        expected.put(vertexList.get(3),new HashSet<>(Arrays.asList(vertexList.get(0),vertexList.get(1),vertexList.get(2))));
        expected.put(vertexList.get(4),new HashSet<>(Arrays.asList(vertexList.get(1),vertexList.get(2),vertexList.get(3))));
        expected.put(vertexList.get(5),new HashSet<>(Arrays.asList(vertexList.get(7),vertexList.get(9))));
        expected.put(vertexList.get(6),new HashSet<>(Arrays.asList(vertexList.get(5),vertexList.get(8))));
        expected.put(vertexList.get(7),new HashSet<>(Arrays.asList(vertexList.get(5),vertexList.get(6),vertexList.get(8))));
        expected.put(vertexList.get(8),new HashSet<>(Arrays.asList(vertexList.get(5),vertexList.get(6),vertexList.get(7))));
        expected.put(vertexList.get(9),new HashSet<>());
        expected.put(vertexList.get(10),new HashSet<>(Arrays.asList(vertexList.get(6),vertexList.get(7),vertexList.get(8))));

        Map<Vertex, Set<Vertex>> knn = NearestNeighbour.knn(3, testGraph);
        Map<Vertex, Set<Vertex>> res = NearestNeighbour.rknn(knn);

        for(Map.Entry<Vertex, Set<Vertex>> entry : res.entrySet()) {
            assertReflectionEquals(entry.getValue(), expected.get(entry.getKey()), ReflectionComparatorMode.LENIENT_ORDER);
        }
    }
    @Test
    public void  testMknn()
    {
        Map<Vertex, Set<Vertex>> expected = new HashMap<>() ;
        expected.put(vertexList.get(0),new HashSet<>());
        expected.put(vertexList.get(1),new HashSet<>());
        expected.put(vertexList.get(2),new HashSet<>());
        expected.put(vertexList.get(3),new HashSet<>());
        expected.put(vertexList.get(4),new HashSet<>());
        expected.put(vertexList.get(5),new HashSet<>(Arrays.asList(vertexList.get(7))));
        expected.put(vertexList.get(6),new HashSet<>(Arrays.asList(vertexList.get(8))));
        expected.put(vertexList.get(7),new HashSet<>(Arrays.asList(vertexList.get(5),vertexList.get(8))));
        expected.put(vertexList.get(8),new HashSet<>(Arrays.asList(vertexList.get(6),vertexList.get(7))));
        expected.put(vertexList.get(9),new HashSet<>());
        expected.put(vertexList.get(10),new HashSet<>());

        Map<Vertex, Set<Vertex>> knn = NearestNeighbour.knn(3, testGraph);
        Map<Vertex, Set<Vertex>> rknn = NearestNeighbour.rknn(knn);
        Map<Vertex, Set<Vertex>> res = NearestNeighbour.mknn(knn,rknn);

        for(Map.Entry<Vertex, Set<Vertex>> entry : res.entrySet()) {
            assertReflectionEquals(entry.getValue(), expected.get(entry.getKey()), ReflectionComparatorMode.LENIENT_ORDER);
        }
    }
}

