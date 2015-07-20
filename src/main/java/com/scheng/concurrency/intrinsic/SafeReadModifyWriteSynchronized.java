package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/19/2015.
 */
public class SafeReadModifyWriteSynchronized {
	private int number;

	public synchronized void incrementNumber() {
		number++;
	}

	public synchronized int getNumber() {
		return this.number;
	}

	public static void main(String[] args) throws InterruptedException {
		final SafeReadModifyWriteSynchronized rmw = new SafeReadModifyWriteSynchronized();

		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					rmw.incrementNumber();
				}

			}, "T" + i).start();
		}

		Thread.sleep(4000);
		System.out.println("Final number (should be 1000): " + rmw.getNumber());
	}
}