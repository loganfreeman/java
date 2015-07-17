package com.scheng.concurrency.phaser;

/**
 * Created by scheng on 7/16/2015.
 */
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Task implements Runnable {

	// 这个任务将在每个阶段休眠的时间
	private int time;

	private Phaser phaser;

	public Task(int time, Phaser phaser) {
		this.time = time;
		this.phaser = phaser;
	}


	/**
	 * 执行3个阶段.在每个阶段,休眠指定的时间.
	 */
	public void run() {
		/*
		 * 达到phaser
		 */
		phaser.arrive();

		/*
		 * 阶段1
		 */
		System.out.printf("%s: Entering phase 1.\n",Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: Finishing phase 1.\n",Thread.currentThread().getName());
		/*
		 * 阶段1结束
		 */
		phaser.arriveAndAwaitAdvance();

		/*
		 * 阶段2
		 */
		System.out.printf("%s: Entering phase 2.\n",Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: Finishing phase 2.\n",Thread.currentThread().getName());
		/*
		 * 阶段2结束
		 */
		phaser.arriveAndAwaitAdvance();

		/*
		 * 阶段3
		 */
		System.out.printf("%s: Entering phase 3.\n",Thread.currentThread().getName());
		try {
			TimeUnit.SECONDS.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("%s: Finishing phase 3.\n",Thread.currentThread().getName());
		/*
		 * 阶段3结束
		 */
		phaser.arriveAndDeregister();
	}

    public static void main(String[] args) throws InterruptedException {
    		Phaser phaser = new Phaser(3);

    		for (int i = 0; i < 3; i++) {
    			Task task = new Task(i + 1, phaser);
    			Thread thread = new Thread(task);
    			thread.start();
    		}

    		// Êä³öPhaserµÄÐÅÏ¢
    		for (int i = 0; i < 10; i++) {
    			System.out.printf("********************\n");
    			System.out.printf("Main: Phaser Log\n");
    			System.out.printf("Main: Phaser: Phase: %d\n", phaser.getPhase());
    			System.out.printf("Main: Phaser: Registered Parties: %d\n", phaser.getRegisteredParties());
    			System.out.printf("Main: Phaser: Arrived Parties: %d\n", phaser.getArrivedParties());
    			System.out.printf("Main: Phaser: Unarrived Parties: %d\n", phaser.getUnarrivedParties());
    			System.out.printf("********************\n");

    			TimeUnit.SECONDS.sleep(1);
    		}
    	}

}
