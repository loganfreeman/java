package com.scheng.concurrency.activeobject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by scheng on 7/17/2015.
 */
public class SampleActiveObjectImpl {

	public String doProcess(String arg, int i) {
		try {
			// 模拟一个比较耗时的操作
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return arg + "-" + i;
	}

    public static void main(String[] args) throws InterruptedException,
            ExecutionException {

    		SampleActiveObject sao = ActiveObjectProxy.newInstance(
    		    SampleActiveObject.class, new SampleActiveObjectImpl(),
    		    Executors.newCachedThreadPool());
    		Future<String> ft = null;
    		try {
    			ft = sao.process("Something", 1);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		Thread.sleep(500);
    		System.out.println(ft.get());
    	}

}