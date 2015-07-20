package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/19/2015.
 */
public class SafeCheckThenAct {
	private int number;

	public synchronized void changeNumber() {
		if (number == 0) {
			System.out.println(Thread.currentThread().getName() + " | Changed");
			number = -1;
		}
		else {
			System.out.println(Thread.currentThread().getName() + " | Not changed");
		}
	}

	public static void main(String[] args) {
		final SafeCheckThenAct checkAct = new SafeCheckThenAct();

		for (int i = 0; i < 50; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					checkAct.changeNumber();
				}

			}, "T" + i).start();
		}
	}
}