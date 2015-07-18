package com.scheng.concurrency.ref;

/**
 * Created by scheng on 7/17/2015.
 */
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;

public class KeyedSoftReference<K, V> extends SoftReference<V> implements IKeyedReference<K, V> {
	private final K mKey;
	public KeyedSoftReference(K pKey, V referent, ReferenceQueue<? super V> q) {
		super(referent, q);
		mKey = pKey;
	}

	@Override
	public K getKey() {
		return mKey;
	}
}