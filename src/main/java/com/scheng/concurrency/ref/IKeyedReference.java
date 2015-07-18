package com.scheng.concurrency.ref;

/**
 * Created by scheng on 7/17/2015.
 */
public interface IKeyedReference<K, V> {
	public K getKey();
	public V get();
}