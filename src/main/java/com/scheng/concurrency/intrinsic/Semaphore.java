package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.Arrays;

public class Semaphore {

  final int MAX_AVAILABLE;
  int available;

  static int count;

  public Semaphore(int ma) {
    this.MAX_AVAILABLE = ma;
    this.available = this.MAX_AVAILABLE;
  }

  public synchronized boolean acquire() {
    if (available > 0) {
      available--;
      return true;
    } else {
      return false;
    }
  }

  public synchronized void release() {
    available++;
    this.notifyAll();
  }

  public static void main(String[] args) {
    final Semaphore s = new Semaphore(1);

    Thread[] threads = new Thread[10];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new MySemaphoredThread(s) {
        public void run(){
          while(!s.acquire()){
            try {
              wait();
            } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
          for(int i = 0; i <= 1000; i++) {
            count++;
          }
          s.release();
        }
      };
    }
    for (Thread t : threads) {
      t.start();
    }
    System.out.println(count);
  }

  private static class MySemaphoredThread extends Thread {
    Semaphore s;
    public MySemaphoredThread (Semaphore s) {
      this.s = s;
    }
  }
}