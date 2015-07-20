package com.scheng.concurrency.intrinsic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by scheng on 7/19/2015.
 */
public class SafeReadModifyWriteAtomic {
	private final AtomicInteger number = new AtomicInteger();

	public void incrementNumber() {
		number.getAndIncrement();
	}

	public int getNumber() {return this.number.get();}

	public static void main(String[] args) throws InterruptedException {
		final SafeReadModifyWriteAtomic rmw = new SafeReadModifyWriteAtomic();

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