package com.scheng.java8.function;

import java.util.function.Predicate;

/**
 * Created by scheng on 7/20/2015.
 */
public class Visa {

    public boolean checkVisaCode(Predicate<Integer> pred, Integer param) {

        return pred.test(param);

    }

    public static void main(String[] args) {

            Visa visa = new Visa();

            boolean sonuc = visa.checkVisaCode(s -> (s >= 400), 405);

            System.out.println("Test result: " + sonuc);
        }
}