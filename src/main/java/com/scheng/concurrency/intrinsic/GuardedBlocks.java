package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/21/2015.
 */
public class GuardedBlocks {

  public static int waitIncrement = 0;
  public static boolean flag;

  public static void main(String[] args) {
    final Object lock = new Object();

    Thread waitForMe = new Thread() {
      public void run() {
        synchronized(lock) {
          for (int i = 0; i < 1000000; i++) {
            waitIncrement++;
          }
          System.out.println("done adding");
          flag = true;
          lock.notifyAll();
        }
      }
    };

    Thread illWaitForYou = new Thread() {
      public void run() {
        synchronized(lock) {
          while (!flag) {
            try {
              System.out.println("waiting...");
              lock.wait();
            } catch (InterruptedException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
          System.out.println(waitIncrement);
        }
      }
    };

    illWaitForYou.start();
    waitForMe.start();
  }
}