package com.scheng.concurrency.phaser;

/**
 * Created by scheng on 7/16/2015.
 */
import java.util.concurrent.Phaser;

public class PhaserTest1 {

    public static void main(String args[]) {
        //
        final int count = 5;
        final Phaser phaser = new Phaser(count);
        for(int i = 0; i < count; i++) {
            System.out.println("starting thread, id: " + i);
            final Thread thread = new Thread(new Task(i, phaser));
            thread.start();
        }
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