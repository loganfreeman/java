package com.scheng.java8;

import java.util.function.BiFunction;

/**
 * Created by scheng on 7/20/2015.
 */
public class Calculator {
    public String calc(BiFunction<Integer, Integer, String> bi, Integer i1, Integer i2) {
        return bi.apply(i1, i2);
    }

    public static void main(String[] args) {

            Calculator calculator = new Calculator();
            String result = calculator.calc((a, b) -> "Result: " + (a * b), 3, 5);

            System.out.println(result);
        }
}