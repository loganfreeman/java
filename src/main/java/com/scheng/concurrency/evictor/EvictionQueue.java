package com.scheng.concurrency.evictor;

/**
 * Created by scheng on 7/18/2015.
 */
public interface EvictionQueue<K, V> {

    /**
     * Returns <tt>true</tt> if this queue contains any entries.
     *
     * @return <tt>true</tt> if this queue contains any entries
     */
    public boolean hasEntries();

    /**
     * Returns the next eviction time of all entries contained in the queue, or 0 if the queue is
     * empty. Since the queue is sorted by the entries eviction time, this is the eviction time of
     * the first entry (head).
     *
     * @return the next eviction time of all entries contained in the queue, or 0 if the queue is
     * empty
     */
    public long getNextEvictionTime();

    /**
     * Puts the specified evictible entry into the queue.
     *
     * @param e the entry to be put into the queue.
     * @throws NullPointerException if the entry is null
     */
    public void putEntry(EvictibleEntry<K, V> e);

    /**
     * Removes the specified evictible entry from the queue.
     *
     * @param e the entry to be removed from the queue.
     * @throws NullPointerException if the entry is null
     */
    public void removeEntry(EvictibleEntry<K, V> e);

    /**
     * Evicts all entries that have expired from their maps and removes them from the queue.
     *
     * @return <tt>true</tt> if any entries have been evicted
     */
    public boolean evictEntries();
}