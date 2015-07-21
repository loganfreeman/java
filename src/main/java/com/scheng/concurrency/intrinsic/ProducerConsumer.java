package com.scheng.concurrency.intrinsic;

/**
 * Created by scheng on 7/21/2015.
 */
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class ProducerConsumer {

  static final Queue<Integer> sharedQueue = new LinkedList<Integer>();

  public static void main(String[] args) {
    Thread p = new Thread(new Producer());
    Thread q = new Thread(new Consumer());

    p.start();
    q.start();
  }

  private static class Producer extends Thread {
    public void produce() {
      Random g = new Random();
      sharedQueue.add(g.nextInt(100));
    }

    public void run() {
      synchronized(sharedQueue) {
        while (true) {
          while (sharedQueue.size() > 5) {
            try {
              sharedQueue.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          produce();
          sharedQueue.notifyAll();
        }
      }
    }
  }

  private static class Consumer extends Thread {
    public void consume() {
      System.out.println(sharedQueue.poll());
    }

    public void run() {
      while (true) {
        synchronized(sharedQueue) {
          while (sharedQueue.size() == 0) {
            try {
              sharedQueue.wait();
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          consume();
          sharedQueue.notifyAll();
        }
      }
    }
  }
}