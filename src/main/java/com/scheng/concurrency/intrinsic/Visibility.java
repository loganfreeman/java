package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/19/2015.
 */
public class Visibility {
	private static volatile boolean ready;

	public static void main(String[] args) throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (ready) {
						System.out.println("Reader Thread - Flag change received. Finishing thread.");
						break;
					}
				}
			}
		}).start();

		Thread.sleep(3000);
		System.out.println("Writer thread - Changing flag...");
		ready = true;
	}
}