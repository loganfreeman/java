package com.scheng.concurrency;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by scheng on 7/16/2015.
 */
public class MyTask implements Runnable {
	private Lock lock;

	public MyTask(Lock lock) {
		this.lock = lock;
	}

	public void run() {
		for (int i = 0; i < 5; i++) {
			lock.lock();

			System.out.printf("%s: Get the  Lock.\n", Thread.currentThread().getName());

			try {
				TimeUnit.MILLISECONDS.sleep(500);
				System.out.printf("%s: Free the Lock.\n", Thread.currentThread().getName());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				// ÊÍ·ÅËø
				lock.unlock();
			}
		}
	}

    public static void main(String[] args) throws InterruptedException {
    		MyLock lock = new MyLock();

    		// 创建5个线程
    		Thread[] threads = new Thread[5];

    		for (int i = 0; i < 5; i++) {
    			MyTask task = new MyTask(lock);
    			threads[i] = new Thread(task);
    			threads[i].start();
    		}

    		for (int i = 0; i < 15; i++) {
    			/*
    			 * 输出关于锁的信息
    			 */
    			System.out.printf("Main: Logging the Lock\n");
    			System.out.printf("************************\n");
    			System.out.printf("Lock: Owner : %s\n", lock.getOwnerName());
    			System.out.printf("Lock: Queued Threads: %s\n", lock.hasQueuedThreads());
    			if (lock.hasQueuedThreads()) {
    				System.out.printf("Lock: Queue Length: %d\n", lock.getQueueLength());
    				System.out.printf("Lock: Queued Threads: ");
    				Collection<Thread> lockedThreads = lock.getThreads();
    				for (Thread lockedThread : lockedThreads) {
    					System.out.printf("%s ", lockedThread.getName());
    				}
    				System.out.printf("\n");
    			}
    			System.out.printf("Lock: Fairness: %s\n", lock.isFair());
    			System.out.printf("Lock: Locked: %s\n", lock.isLocked());
    			System.out.printf("************************\n");

    			TimeUnit.SECONDS.sleep(1);
    		}

    	}

}