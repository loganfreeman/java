package com.scheng.java8.function;

import java.util.function.Function;

/**
 * Created by scheng on 7/20/2015.
 */
public class Calculator {
    public String calc(Function<Integer, String> bi, Integer i) {
        return bi.apply(i);
    }
    public static void main(String[] args) {

            Calculator calculator = new Calculator();
            String result = calculator.calc((a) -> "Result: " + (a * 2), 10);

            System.out.println(result);
        }
}