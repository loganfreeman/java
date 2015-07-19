package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/19/2015.
 */
public class MyPrivateLockExample extends PrivateLockExample {

	public synchronized void executeAnotherTask() throws InterruptedException {
		System.out.println("executeAnotherTask - Entering...");
		Thread.sleep(3000);
		System.out.println("executeAnotherTask - Exiting...");
	}

	public static void main(String[] args) {
		MyPrivateLockExample privateLock = new MyPrivateLockExample();

		Thread t1 = new Thread(new Worker1(privateLock));
		Thread t2 = new Thread(new Worker2(privateLock));

		t1.start();
		t2.start();
	}

	private static class Worker1 implements Runnable {
		private final MyPrivateLockExample privateLock;

		public Worker1(MyPrivateLockExample privateLock) {
			this.privateLock = privateLock;
		}

		@Override
		public void run() {
			try {
				privateLock.executeTask();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static class Worker2 implements Runnable {
		private final MyPrivateLockExample privateLock;

		public Worker2(MyPrivateLockExample privateLock) {
			this.privateLock = privateLock;
		}

		@Override
		public void run() {
			try {
				privateLock.executeAnotherTask();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}