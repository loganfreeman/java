package com.scheng.concurrency.forkjoin;

import java.util.Random;

/**
 * Created by scheng on 7/21/2015.
 */
public class ArrayGenerator {
    public int[] generateArray(int size) {
    		int array[] = new int[size];
    		Random random = new Random();
    		for (int i = 0; i < size; i++) {
    			array[i] = random.nextInt(10);
    		}
    		return array;
    	}
}
