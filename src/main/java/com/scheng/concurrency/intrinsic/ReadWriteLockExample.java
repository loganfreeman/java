package com.scheng.concurrency.intrinsic;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by scheng on 7/19/2015.
 */
public class ReadWriteLockExample {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteLockExample.class);
	final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private Data data = new Data("default value");
	
	public static void main(String[] args) {
		ReadWriteLockExample example = new ReadWriteLockExample();
		example.start();
	}
	
	private void start() {
		ExecutorService service = Executors.newFixedThreadPool(4);
		for (int i=0; i<3; i++) service.execute(new ReadWorker());
		service.execute(new WriteWorker());
		
		service.shutdown();
	}
	
	class ReadWorker implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 2; i++) {
				readWriteLock.readLock().lock();
				try {
					LOGGER.info("{}|Read lock acquired", Thread.currentThread().getName());
					Thread.sleep(3000);
					LOGGER.info("{}|Reading data: {}", Thread.currentThread().getName(), data.getValue());
				} catch (InterruptedException e) {
					//handle interrupted
				} finally {
					readWriteLock.readLock().unlock();
				}
			}
		}
	}
	
	class WriteWorker implements Runnable {
		@Override
		public void run() {
			readWriteLock.writeLock().lock();
			try {
				LOGGER.info("{}|Write lock acquired", Thread.currentThread().getName());
				Thread.sleep(3000);
				data.setValue("changed value");
				LOGGER.info("{}|Writing data: changed value", Thread.currentThread().getName());
			} catch (InterruptedException e) {
				//handle interrupted
			} finally {
				readWriteLock.writeLock().unlock();
			}
		}
	}

    private class Data {
        private String value;

        	public Data(String value) {
        		this.value = value;
        	}

        	public String getValue() {
        		return this.value;
        	}

        	public void setValue(String value) {
        		this.value = value;
        	}
    }
}