package com.scheng.concurrency.ref;

/**
 * Created by scheng on 7/17/2015.
 */

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class CheckWhenAnObjectIsNoLongerReferenced {

    private static Object object;

    public static void main(String[] args) {

        object = new Object();

        // Reference queue, to which registered reference objects are appended by the
        // garbage collector after the appropriate reachability changes are detected.
        ReferenceQueue<Object> rq = new ReferenceQueue<Object>();

        // Create a new weak reference that refers to the given object and is registered with this queue.
        WeakReference<Object> wr = new WeakReference<Object>(object, rq);

        // start a new thread that will remove all references of object
        new Thread(runnable).start();

        // wait for all the references to the object to be removed
        try {
            while (true) {

                Reference<?> r = rq.remove();

                if (r == wr) {


                    System.out.println("Object is no longer referenced.");

                }

                break;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println("Setting object to null");
                object = null;
                System.out.println("Running Garbage Collection...");
                Runtime.getRuntime().gc(); // run GC to collect the object
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

}