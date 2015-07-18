package com.scheng.concurrency.evictor;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.Map.Entry;

/**
 * Created by scheng on 7/18/2015.
 */
public class EvictibleEntry<K, V> implements Entry<K, V> {

    private final ConcurrentMapWithTimedEvictionDecorator<K, V> map;
    private final K key;
    private volatile V value;
    private final long evictMs;
    private final boolean evictible;
    private final long evictionTime;
    private volatile Object data;

    /**
     * Creates a new evictible entry with the specified key, value, and eviction time.
     *
     * @param map map that will contain the entry being created
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @param evictMs the time in ms during which the entry can stay in the map (time-to-live). When
     * this time has elapsed, the entry will be evicted from the map automatically. A value of 0 for
     * this argument means "forever".
     * @throws NullPointerException if the value is null
     * @throws IllegalArgumentException if evictMs is negative
     */
    EvictibleEntry(ConcurrentMapWithTimedEvictionDecorator<K, V> map, K key, V value, long evictMs) {
        if (value == null)
            throw new NullPointerException();
        if (evictMs < 0)
            throw new IllegalArgumentException();
        this.map = map;
        this.key = key;
        this.value = value;
        this.evictMs = evictMs;
        this.evictible = (evictMs > 0);
        this.evictionTime = (evictible) ? System.nanoTime()
            + NANOSECONDS.convert(evictMs, MILLISECONDS) : 0;
    }

    /**
     * Returns the key corresponding to this entry.
     *
     * @return the key corresponding to this entry
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * Returns the value corresponding to this entry.
     *
     * @return the value corresponding to this entry
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * Replaces the value corresponding to this entry with the specified value. (Writes through to
     * the map.)
     *
     * @param value new value to be stored in this entry
     * @return old value corresponding to the entry
     * @throws ClassCastException if the class of the specified value prevents it from being stored
     * in the backing map
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public synchronized V setValue(V value) {
        if (value == null)
            throw new NullPointerException();
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    /**
     * Returns true if the entry is evictible. An entry is evictible if the value of
     * <tt>evictMs</tt> specified upon its construction was positive.
     *
     * @return <tt>true</tt> if the entry is evictible
     */
    public boolean isEvictible() {
        return evictible;
    }

    /**
     * Returns the eviction time of this entry, in nanoseconds. This time is calculated upon
     * creation by adding the specified <tt>evictMs</tt> to the current time returned by
     * <tt>System.nanoTime()</tt>.
     *
     * @return the eviction time of this entry in nanoseconds.
     */
    public long getEvictionTime() {
        return evictionTime;
    }

    /**
     * Returns the additional data associated with this entry.
     *
     * @return the additional data associated with this entry
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the additional data associated with this entry.
     *
     * @param data additional data associated with this entry
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * Returns true if the entry should be evicted. An entry should be evicted if it is evictible
     * and its eviction time is less than <tt>System.nanoTime()</tt>.
     *
     * @return <tt>true</tt> if the entry should be evicted
     */
    public boolean shouldEvict() {
        return (evictible) ? (System.nanoTime() > evictionTime) : false;
    }

    /**
     * Actually evicts the entry from its map by calling the <tt>evict</tt> method on the map,
     * passing the specified flag.
     *
     * @param cancelPendingEviction <tt>true</tt> if any pending evictions of the entry should be
     * cancelled
     */
    public void evict(boolean cancelPendingEviction) {
        map.evict(this, cancelPendingEviction);
    }

    @Override
    public String toString() {
        return String.format("[%s, %s, %d]", (key != null) ? key : "null", value, evictMs);
    }

}