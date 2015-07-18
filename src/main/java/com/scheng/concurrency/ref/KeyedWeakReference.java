package com.scheng.concurrency.ref;

/**
 * Created by scheng on 7/17/2015.
 */
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class KeyedWeakReference<K, V> extends WeakReference<V> implements IKeyedReference<K, V> {
	private final K mKey;
	public KeyedWeakReference(K pKey, V referent, ReferenceQueue<? super V> q) {
		super(referent, q);
		mKey = pKey;
	}

	@Override
	public K getKey() {
		return mKey;
	}
}