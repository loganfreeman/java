package com.scheng;



import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }

    /**
     *
     * the input is a string, which contains a rectangular grid of numbers
     * each line is a sequence of digits.
     * a grid cell has a edge to its adjacent cell ( including diagonals ) if the value is less than the value of its adjacent cell
     * @param s
     * @return
     */
    public static Graph importGraph(String s, int dim){
        Graph graph = new Graph();
        int[][] grid = new int[dim][dim];
        for(String line : s.split("/n"));
        return graph;
    }

    public static String streamToString(InputStream inputStream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer, "UTF-8");
        return writer.toString();
    }

}
