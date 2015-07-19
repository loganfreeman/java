package com.scheng.concurrency.util;

/**
 * Created by scheng on 7/18/2015.
 */
public final class WithException<E extends Exception> {

    private final Class<E> type;

    public static <E extends Exception> WithException<E> with(Class<E> type) {
        return new WithException<E>(type);
    }

    /** @since 1.2 */
    public static <E extends Exception> WithException<E> as(Class<E> type) {
        return new WithException<E>(type);
    }

    private WithException(Class<E> type) {
        this.type = type;
    }

    public Class<E> getType() {
        return type;
    }
}