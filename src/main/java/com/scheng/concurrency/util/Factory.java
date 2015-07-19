package com.scheng.concurrency.util;

/**
 * Created by scheng on 7/18/2015.
 */
public interface Factory<T> {
    T create() throws FactoryException;
}