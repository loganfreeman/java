package com.scheng.concurrency.phaser;

/**
 * Created by scheng on 7/16/2015.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Phaser;

public class PhaserTest2 {

    public static void main(String args[]) throws Exception {
        //
        final Phaser phaser = new Phaser(1);
        for(int i = 0; i < 5; i++) {
            phaser.register();
            System.out.println("starting thread, id: " + i);
            final Thread thread = new Thread(new Task(i, phaser));
            thread.start();
        }

        //
        System.out.println("Press ENTER to continue");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.readLine();
        phaser.arriveAndDeregister();
    }

    public static class Task implements Runnable {
        //
        private final int id;
        private final Phaser phaser;

        public Task(int id, Phaser phaser) {
            this.id = id;
            this.phaser = phaser;
        }

        public void run() {
            phaser.arriveAndAwaitAdvance();
            System.out.println("in Task.run(), phase: " + phaser.getPhase() + ", id: " + this.id);
        }
    }
}