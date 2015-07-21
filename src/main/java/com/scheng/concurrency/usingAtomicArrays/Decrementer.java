package com.scheng.concurrency.usingAtomicArrays;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by scheng on 7/21/2015.
 */
public class Decrementer implements Runnable {

	private AtomicIntegerArray vector;

	public Decrementer(AtomicIntegerArray vector) {
		this.vector = vector;
	}


	@Override
	public void run() {
		for (int i = 0; i < vector.length(); i++) {
			vector.getAndDecrement(i);
		}

	}

}