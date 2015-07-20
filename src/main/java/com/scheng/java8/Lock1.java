package com.scheng.java8;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

/**
 * Created by scheng on 7/20/2015.
 */
public class Lock1 {

    private static final int NUM_INCREMENTS = 10000;

    private static ReentrantLock lock = new ReentrantLock();

    private static int count = 0;

    private static void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        testLock();
        testTryLock();
        testReadWriteLock();
        testStampedLock();
    }

    public static void testStampedLock(){
        ExecutorService executor = Executors.newFixedThreadPool(2);

                Map<String, String> map = new HashMap<>();

                StampedLock lock = new StampedLock();

                executor.submit(() -> {
                    long stamp = lock.writeLock();
                    try {
                        ConcurrentUtils.sleep(1);
                        map.put("foo", "bar");
                    } finally {
                        lock.unlockWrite(stamp);
                    }
                });

                Runnable readTask = () -> {
                    long stamp = lock.readLock();
                    try {
                        System.out.println(map.get("foo"));
                        ConcurrentUtils.sleep(1);
                    } finally {
                        lock.unlockRead(stamp);
                    }
                };
                executor.submit(readTask);
                executor.submit(readTask);

                ConcurrentUtils.stop(executor);
    }

    public  static void testReadWriteLock(){
        ExecutorService executor = Executors.newFixedThreadPool(2);

                Map<String, String> map = new HashMap<>();

                ReadWriteLock lock = new ReentrantReadWriteLock();

                executor.submit(() -> {
                    lock.writeLock().lock();
                    try {
                        ConcurrentUtils.sleep(1);
                        map.put("foo", "bar");
                    } finally {
                        lock.writeLock().unlock();
                    }
                });

                Runnable readTask = () -> {
                    lock.readLock().lock();
                    try {
                        System.out.println(map.get("foo"));
                        ConcurrentUtils.sleep(1);
                    } finally {
                        lock.readLock().unlock();
                    }
                };
                executor.submit(readTask);
                executor.submit(readTask);

                ConcurrentUtils.stop(executor);
    }

    public static void  testTryLock(){
        ExecutorService executor = Executors.newFixedThreadPool(2);

                ReentrantLock lock = new ReentrantLock();

                executor.submit(() -> {
                    lock.lock();
                    try {
                        ConcurrentUtils.sleep(1);
                    } finally {
                        lock.unlock();
                    }
                });

                executor.submit(() -> {
                    System.out.println("Locked: " + lock.isLocked());
                    System.out.println("Held by me: " + lock.isHeldByCurrentThread());
                    boolean locked = lock.tryLock();
                    System.out.println("Lock acquired: " + locked);
                });

                ConcurrentUtils.stop(executor);
    }

    private static void testLock() {
        count = 0;

        ExecutorService executor = Executors.newFixedThreadPool(2);

        IntStream.range(0, NUM_INCREMENTS)
                 .forEach(i -> executor.submit(Lock1::increment));

        ConcurrentUtils.stop(executor);

        System.out.println(count);
    }

}