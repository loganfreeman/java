package com.scheng.concurrency.evictor;

/**
 * Created by scheng on 7/18/2015.
 */
public interface EvictionScheduler<K, V> {

    /**
     * Schedules the eviction of the specified entry from its map. This method is called by the
     * associated instances of {@link ConcurrentMapWithTimedEvictionDecorator} just after a new
     * entry has been added. The entry is not guaranteed to be evictible, it may also be a permanent
     * entry. Therefore, the implementation should check if this entry is evictible before doing any
     * scheduling.
     *
     * @param e the entry for which the eviction should be scheduled, if evictible; it must have
     * been already added to its map
     * @throws NullPointerException if the entry is null
     */
    public void scheduleEviction(EvictibleEntry<K, V> e);

    /**
     * Cancels the eviction of the specified entry from its map. This method is called by the
     * associated instances of {@link ConcurrentMapWithTimedEvictionDecorator} just after an entry
     * has been removed. The entry is not guaranteed to be evictible, it may also be a permanent
     * entry. Therefore, the implementation should check if this entry is evictible before doing any
     * cancellation.
     *
     * @param e the entry for which the eviction should be cancelled, if evictible; it must have
     * been already removed from its map
     * @throws NullPointerException if the entry is null
     */
    public void cancelEviction(EvictibleEntry<K, V> e);

    /**
     * Immediately shuts down the scheduler and cancels all pending evictions. After it has been
     * shut down, the scheduler cannot be used any more. This method is never called by the
     * associated instances of {@link ConcurrentMapWithTimedEvictionDecorator}. Instead, it is
     * intended to be called by the client that has created the scheduler to properly shut it down
     * when it's not needed any more.
     */
    public void shutdown();
}