package com.scheng;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.scheng.Graph.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testImportGrid() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        graph.fromGrid(input, 3, false);
    }

    public void testTopoligicalSort() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        graph.fromGrid(input, 3, false);
        graph.topologicalSort();
    }

    public void testLongestPath() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        graph.fromGrid(input, 3, false);
        graph.printLongestPath();
    }

    public void testPrintAllPossiblePath() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        graph.fromGrid(input, 3, false);
        graph.printAllPossibleLongestPath();

    }

    public void testPrintAllPossiblePathAllowTransitive() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        graph.fromGrid(input, 3, true);
        graph.printAllPossibleLongestPath();

    }


    /**
     *
     *
     * since there exists cycle in the graph, this won't work, but with cycle detection, it should work
     * @throws IOException
     */
    public void testPrintAllPossiblePathBruteForce() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        graph.fromGrid(input, 3, true);
        graph.printAllPossibleLongestPathBruteForce();

    }

    public void testPrintAllPossiblePathBruteForceNoCycle() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid2.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        graph.fromGrid(input, 3, true);
        graph.printAllPossibleLongestPathBruteForce();

    }

    public void testAll() throws IOException {
        // System.out.println("Entry");
        InputStream stream = AppTest.class.getResourceAsStream("/grid.txt");
        //System.out.println(App.streamToString(stream));
        String input = App.streamToString(stream);
        Graph graph = new Graph();
        int[][] grid = new int[3][3];
        int row = 0, col =0;
        for(String line : input.split("\\n")){
            col = 0;
            //System.out.println(line);
            for(String d : line.split("\\s+")){
                int v = Integer.valueOf(d.trim(), 10);
                grid[row][col++]= v;
            }

            row++;
        }



        Node[][] nodes = new Node[3][3];

        row = 0;
        col = 0;
        for(int[] r : grid){

            col =0;
            for(int c : r){
                //System.out.print(c + "\t");
                Node node = new Node(c);
                graph.addNode(node);
                nodes[row][col++] = node;


            }

            row++;

            //System.out.println();
        }




        row = 0;
        col = 0;
        for(Node[] r : nodes){
            col =0;
            for(Node n : r){
                System.out.print(n.name + "\t");
                if(row -1 >=0){
                    Node other = nodes[row-1][col];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }

                    if(col - 1 >=0) {
                        other = nodes[row-1][col-1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }
                    }

                    if(col +1 < 3) {
                        other = nodes[row-1][col+1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }
                    }

                }

                if(row + 1 < 3){
                    Node other = nodes[row+1][col];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }

                    if(col - 1 >=0) {
                        other = nodes[row+1][col-1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }
                    }

                    if(col +1 < 3) {
                        other = nodes[row+1][col+1];
                        if(other.name > n.name){
                            n.addEdge(other);
                        }
                    }
                }

                if(col - 1 >=0) {
                    Node other = nodes[row][col-1];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }

                }

                if(col + 1 < 3){
                    Node other = nodes[row][col+1];
                    if(other.name > n.name){
                        n.addEdge(other);
                    }


                }
                col++;

            }
            System.out.println();
            row++;

        }
/*        for(Node node : graph.nodes){
            System.out.println(node.name);
            for(Edge edge : node.outEdges){
                System.out.println(edge);
            }
        }*/
        List<Node> sorted = graph.topologicalSort();

        for(Node node : sorted){
            if(node.getInComingEdges().size() == 0){
                continue;
            }
            int longest = 0;
            Node from = null;
            for(Edge edge : node.getInComingEdges()){
                Node other = edge.from;
                if(other.longesetPath >= longest){
                    longest = other.longesetPath;
                    from = other;
                }
            }
            node.longesetPath = from.longesetPath + 1;
            node.path.addAll(from.path);
            node.path.add(from);
        }

        System.out.println("...............................");

        for(Node node : sorted){
            node.printPath();
        }






    }
}
