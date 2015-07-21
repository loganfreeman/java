package com.scheng.concurrency.usingAtomicArrays;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Created by scheng on 7/21/2015.
 */
public class Incrementer implements Runnable {

	private AtomicIntegerArray vector;

	public Incrementer(AtomicIntegerArray vector) {
		this.vector = vector;
	}

	@Override
	public void run() {
		for (int i = 0; i < vector.length(); i++) {
			vector.getAndIncrement(i);
		}

	}

}