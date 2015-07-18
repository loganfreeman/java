package com.scheng.concurrency.evictor;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by scheng on 7/18/2015.
 */
public class DelayedTaskEvictionScheduler<K, V> extends AbstractQueueEvictionScheduler<K, V> {

    public static final int DEFAULT_THREAD_POOL_SIZE = 1;

    private final ScheduledExecutorService ses;
    private volatile ScheduledFuture<?> future = null;
    private volatile long next = 0;

    /**
     * Creates a delayed task eviction scheduler with the default queue implementation (see
     * {@link AbstractQueueEvictionScheduler}) and a
     * {@link java.util.concurrent.ScheduledThreadPoolExecutor}.
     */
    public DelayedTaskEvictionScheduler() {
        this(new ScheduledThreadPoolExecutor(DEFAULT_THREAD_POOL_SIZE));
    }

    /**
     * Creates a delayed task eviction scheduler with the default queue implementation (see
     * {@link AbstractQueueEvictionScheduler}) and the specified scheduled executor service.
     *
     * @param ses the scheduled executor service to be used
     * @throws NullPointerException if the scheduled executor service is null
     */
    public DelayedTaskEvictionScheduler(ScheduledExecutorService ses) {
        super();
        if (ses == null)
            throw new NullPointerException();
        this.ses = ses;
    }

    /**
     * Creates a delayed task eviction scheduler with the specified queue and a
     * {@link java.util.concurrent.ScheduledThreadPoolExecutor}.
     *
     * @param queue the queue to be used
     * @throws NullPointerException if the queue is null
     */
    public DelayedTaskEvictionScheduler(EvictionQueue<K, V> queue) {
        this(queue, new ScheduledThreadPoolExecutor(DEFAULT_THREAD_POOL_SIZE));
    }

    /**
     * Creates a delayed task eviction scheduler with the specified queue and scheduled executor
     * service.
     *
     * @param queue the queue to be used
     * @param ses the scheduled executor service to be used
     * @throws NullPointerException if either the queue or the scheduled executor service is null
     */
    public DelayedTaskEvictionScheduler(EvictionQueue<K, V> queue, ScheduledExecutorService ses) {
        super(queue);
        if (ses == null)
            throw new NullPointerException();
        this.ses = ses;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation simply invokes the <tt>shutdownNow</tt> method on the scheduled executor
     * service.
     */
    @Override
    public void shutdown() {
        ses.shutdownNow();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation checks if the current next eviction time is different from the last next
     * eviction time, and if so (re)schedules the task.
     *
     * @throws RejectedExecutionException if the task cannot be scheduled for execution
     */
    @Override
    protected void onScheduleEviction(EvictibleEntry<K, V> e) {
        if (getNextEvictionTime() != next) {
            scheduleTask();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation checks if the current next eviction time is different from the last next
     * eviction time, and if so (re)schedules the task.
     *
     * @throws RejectedExecutionException if the task cannot be scheduled for execution
     */
    @Override
    protected void onCancelEviction(EvictibleEntry<K, V> e) {
        if (getNextEvictionTime() != next) {
            scheduleTask();
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * This implementation simply always schedules the task.
     *
     * @throws RejectedExecutionException if the task cannot be scheduled for execution
     */
    @Override
    protected void onEvictEntries() {
        schedule();
    }

    /*
     * (Re)schedules the task atomically. This method is synchronized to ensure atomicity.
     */
    private synchronized void scheduleTask() {
        // Cancel the task if already scheduled, then call schedule() to schedule a new task.
        if (future != null) {
            future.cancel(false);
        }
        schedule();
    }

    /*
     * Schedules the task atomically. This method is synchronized to ensure atomicity. The next
     * eviction time is always queried within this synchronized method and not passed as parameter
     * to ensure consistency in a concurrent environment.
     */
    private synchronized void schedule() {
        // Get the next eviction time and reschedule the task with a delay corresponding to the
        // difference between this time and the current time. If the next eviction time is 0
        // (the queue is empty), don't schedule anything.
        next = getNextEvictionTime();
        future = (next > 0) ? ses.schedule(new EvictionRunnable(),
            Math.max(next - System.nanoTime(), 0), NANOSECONDS) : null;
    }

}