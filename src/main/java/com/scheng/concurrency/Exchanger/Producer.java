package com.scheng.concurrency.Exchanger;

/**
 * Created by scheng on 7/21/2015.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;


public class Producer implements Runnable {

    private List<String> buffer;
    private final Exchanger<List<String>> exchanger;

    public Producer(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {

        int cycle = 1;

        for (int i = 0; i < 10; i++) {
            System.out.println("Producer: Cycle " + cycle);

            for (int j = 0; j < 10; j++) {
                String message = "Event " + ((i * 10) + j);
                System.out.println("Producer: " + message);
                buffer.add(message);
            }

            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Producer " + buffer.size());
            cycle++;
        }
    }

    public static void main(String[] args) {

        List<String> buffer1 = new ArrayList<String>();
        List<String> buffer2 = new ArrayList<String>();

        Exchanger<List<String>> exchanger = new Exchanger<List<String>>();

        Producer producer = new Producer(buffer1, exchanger);
        Consumer consumer = new Consumer(buffer2, exchanger);

        Thread threadProducer = new Thread(producer);
        Thread threadConsumer = new Thread(consumer);

        threadProducer.start();
        threadConsumer.start();
    }


}