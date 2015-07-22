package com.scheng.guava;
import com.google.common.util.concurrent.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Created by scheng on 7/22/2015.
 */
public class ListenableFutureExample {

    public static void listenableFutureWithCallbackExample() {
        ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        Callable<String> asyncTask = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "ha! ha!";
            }
        };

        ListenableFuture<String> listenableFuture = executor.submit(asyncTask);

        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println("Success!");
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        // do something else

        try {
            String result = listenableFuture.get();
            System.out.println(result);
        } catch (ExecutionException e) {
            System.out.println("Task failed " + e);
        } catch (InterruptedException e) {
            System.out.println("Task interrupted " + e);
        }
    }


    static ListenableFuture<String> getFlightSchedule(String location) {
        final ListeningExecutorService pool = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        ListenableFuture<String> flight = pool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "HA406";
            }
        });

        return flight;
    }
    static AsyncFunction<String, String> getHotelInformation = new AsyncFunction<String, String>() {
            @Override
            public ListenableFuture<String> apply(String input) throws Exception {
                System.out.println("We will get there by " + input);
                return Futures.immediateFuture("Hotel Waikiki");
            }
    };
    static AsyncFunction<String, List<String>> getAttractions = new AsyncFunction<String, List<String>>() {
        @Override
        public ListenableFuture<List<String>> apply(String input) throws Exception {
            System.out.println("While we stay in " + input);
            List<String> attractions = new ArrayList<>();
            attractions.add("Maui");
            attractions.add("Haleakala");
            attractions.add("Hana Road");
            return Futures.immediateFuture(attractions);
        }
    };
    static AsyncFunction<List<String>, String> getSummary = new AsyncFunction<List<String>, String>() {
        @Override
        public ListenableFuture<String> apply(List<String> input) throws Exception {
            StringBuilder summary = new StringBuilder("We will visit ");
            summary.append(input.toString());
            return Futures.immediateFuture(summary.toString());
        }
    };

    static ListenableFuture<String> getFamilyTripItinerary(String location) {
        ListenableFuture<String> flight = getFlightSchedule(location);
        ListenableFuture<String> hotel = Futures.transform(flight, getHotelInformation);
        ListenableFuture<List<String>> attractions = Futures.transform(hotel, getAttractions);
        return Futures.transform(attractions, getSummary);
    }

    public static void main(String[] args) {
        try {
            System.out.println(getFamilyTripItinerary("Hawaii").get());
        } catch (ExecutionException e) {
            System.out.println("Task failed " + e);
        } catch (InterruptedException e) {
            System.out.println("Task interrupted " + e);
        }
        listenableFutureWithCallbackExample();
    }
}