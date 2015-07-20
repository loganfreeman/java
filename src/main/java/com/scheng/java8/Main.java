package com.scheng.java8;

import java.util.concurrent.*;

/**
 * Created by scheng on 7/20/2015.
 */
public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService ex = Executors.newSingleThreadExecutor();


        Future<Integer> future =
                // This Lambda evaluated to Callable<Integer>
                ex.submit(() -> ThreadLocalRandom.current().nextInt(1, 10));


        System.out.println("Randomized value: " + future.get());

    }
}
