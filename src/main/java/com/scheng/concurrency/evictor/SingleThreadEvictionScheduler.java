package com.scheng.concurrency.evictor;

/**
 * Created by scheng on 7/18/2015.
 */
public class SingleThreadEvictionScheduler<K, V> extends AbstractQueueEvictionScheduler<K, V> {

    private volatile boolean finished = false;
    private volatile boolean notified = false;
    private volatile long next = 0;
    private final Thread t = new Thread(new EvictionThread());
    private final Object m = new Object();

    /**
     * Creates a single thread eviction scheduler with the default queue implementation (see
     * {@link AbstractQueueEvictionScheduler}). This constructor starts the eviction thread, which
     * will remain active until the shutdown method is called.
     */
    public SingleThreadEvictionScheduler() {
        super();
        t.start();
    }

    /**
     * Creates a single thread eviction scheduler with the specified queue. This constructor starts
     * the eviction thread, which will remain active until the shutdown method is called.
     *
     * @param queue the queue to be used
     * @throws NullPointerException if the queue is null
     */
    public SingleThreadEvictionScheduler(EvictionQueue<K, V> queue) {
        super(queue);
        t.start();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation causes the eviction thread to terminate.
     */
    @Override
    public void shutdown() {
        finished = true;
        t.interrupt();
        try {
            t.join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation checks if the current next eviction time is different from the last next
     * eviction time, and if so notifies the eviction thread, causing it to wake-up and recalculate
     * its waiting time.
     */
    @Override
    protected void onScheduleEviction(EvictibleEntry<K, V> e) {
        if (getNextEvictionTime() != next) {
            synchronized (m) {
                notified = true;
                m.notifyAll();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation checks if the current next eviction time is different from the last next
     * eviction time, and if so notifies the eviction thread, causing it to wake-up and recalculate
     * its waiting time.
     */
    @Override
    protected void onCancelEviction(EvictibleEntry<K, V> e) {
        if (getNextEvictionTime() != next) {
            synchronized (m) {
                notified = true;
                m.notifyAll();
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation does nothing.
     */
    @Override
    protected void onEvictEntries() {
        // Do nothing
    }

    /*
     * Eviction thread runnable.
     */
    final class EvictionThread implements Runnable {

        /*
         * Runs the eviction thread.
         */
        @Override
        public void run() {
            while (!finished) {
                next = getNextEvictionTime();
                long timeout = calcTimeout(next);
                while (timeout >= 0) {
                    // The timeout is 0 (forever) or positive - wait
                    if (!waitFor(timeout) && !finished) {
                        // The timeout did not expire and we are not finished -
                        // calculate the next timeout
                        next = getNextEvictionTime();
                        timeout = calcTimeout(next);
                    } else {
                        // The timeout expired or we are finished - get out
                        break;
                    }
                }
                evictEntries();
            }
        }

        /*
         * Calculates the wait timeout (in nanoseconds) to the specified moment in time (in
         * nanoseconds). If the time is 0 (forever), return also 0 (forever). A negative value
         * returned from this method means that no waiting should happen.
         */
        private long calcTimeout(long time) {
            if (time > 0) {
                long x = time - System.nanoTime();
                return (x != 0) ? x : -1;
            } else {
                return 0;
            }
        }

        /*
         * Waits for the specified timeout (in nanoseconds). Returns true if the timeout expired and
         * false if thread was notified or interrupted.
         */
        private boolean waitFor(long timeout) {
            boolean result = true;
            try {
                synchronized (m) {
                    notified = false;
                    m.wait(timeout / 1000000, (int) (timeout % 1000000));
                    result = !notified;
                }
            } catch (InterruptedException e) {
                result = false;
            }
            return result;
        }
    }

}