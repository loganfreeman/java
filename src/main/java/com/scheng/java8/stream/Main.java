package com.scheng.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by scheng on 7/20/2015.
 */
public class Main {
    public static void main(String[] args) {

           // Some Turkish Name & null
           List<String> liste = Arrays.asList("Halil", "İbrahim", "Davut", null);

           liste.parallelStream()// Parallel stuff uses Fork-Join
                   .filter(e -> (!Objects.equals(e, null))) // lazy & parallel
                   .filter(e -> (e.length() > 3)) // lazy & parallel

                   .forEach(e -> {   // sequential & eagerly
                       System.out.println("Bigger length than 3 in List: " + e);
                   });
       }
}
