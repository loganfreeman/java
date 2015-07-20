package com.scheng.java8;

import java.util.function.Consumer;

/**
 * Created by scheng on 7/20/2015.
 */
public class Car {

    public void sayBrand(Consumer<String> block, String param) {
        block.accept(param);
    }

    public static void main(String[] args) {
            Car car = new Car();

            // Data and Code stands same point
            car.sayBrand(e -> System.out.println("Car's Brand: " + e), "Audio A6");

        }
}