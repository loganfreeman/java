package com.scheng.concurrency.ref;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by scheng on 7/17/2015.
 */
public class GCNotifier {
    private final ReferenceQueue<Object> refQueue;
    private final ExecutorService notificationExec;
    // have to hold the weak refs so they don't get GC'ed.
    // if the weak refs are GC'ed, then we don't get notified
    // about the GC of whatever they are referencing.
    private final Map<Reference<?>, Object> refs;
    private static final Object NOTHING = new Object();

    public GCNotifier() {
        refQueue = new ReferenceQueue<Object>();
        notificationExec = Executors.newFixedThreadPool(1, new NamedThreadFactory(GCNotifier.class.getSimpleName()));
        refs = new ConcurrentHashMap<Reference<?>, Object>();

        notificationExec.execute(new Runnable() {
            @Override
            public void run() {
                while (!Thread.interrupted()) {
                    try {
                        KeyedWeakReference<Listener, Object> ref = (KeyedWeakReference<Listener, Object>) refQueue.remove();
                        refs.remove(ref);
                        ref.getKey().objectCollected();
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        });
    }

    public void register(Listener listener, Object object) {
        KeyedWeakReference<Listener, Object> ref = new KeyedWeakReference<Listener, Object>(listener, object, refQueue);
        refs.put(ref, NOTHING);
    }

    public static interface Listener {
        public void objectCollected();
    }

    static Object someImportantObject = new Object();

    public static void main(String[] args) {
        GCNotifier notifier = new GCNotifier();

        notifier.register(new GCNotifier.Listener() {
            @Override
            public void objectCollected() {
                // do something that is relevant to someImportantObject having been collected.
                System.out.println("object gc happened");
            }
        }, someImportantObject);

        new Thread(new Runnable() {
            @Override
            public void run() {
                someImportantObject = null;
                Runtime.getRuntime().gc(); // run GC to collect the object
            }
        }).start();
    }
}