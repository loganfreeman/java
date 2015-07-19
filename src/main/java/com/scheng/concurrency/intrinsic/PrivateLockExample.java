package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/19/2015.
 */
public class PrivateLockExample {
	private Object myLock = new Object();

	public void executeTask() throws InterruptedException {
		synchronized(myLock) {
			System.out.println("executeTask - Entering...");
			Thread.sleep(3000);
			System.out.println("executeTask - Exiting...");
		}
	}
}