package com.scheng.java8;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by scheng on 7/20/2015.
 */
public class CompletableFuture1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = new CompletableFuture<>();

        future.complete("42");

        future
                .thenAccept(System.out::println)
                .thenAccept(v -> System.out.println("done"));

    }

}