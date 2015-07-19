package com.scheng.concurrency.util;

import java.lang.reflect.Constructor;
import java.util.concurrent.Callable;

/**
 * Created by scheng on 7/18/2015.
 */
public class ExceptionWrapper {

    public static <V> V wrapAsRuntimeException(Callable<V> callable) throws RuntimeException {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <V, E extends Exception> V wrapAnyException(Callable<V> callable, final WithException<E> wrapper) throws E {
        try {
            return callable.call();
        } catch (Throwable e) {
            throw ExceptionFactory.newException(wrapper, e).create();
        }
    }

    /** @since 1.2 */
    public static void throwAsRuntimeException(Exception throwable) {
        throw new RuntimeException(throwable);
    }

    /** @since 1.2 */
    public static <E extends RuntimeException> void throwException(Exception throwable, WithException<E> wrapper) throws E {
        throw ExceptionFactory.newException(wrapper, throwable).create();
    }

    static class ExceptionFactory<E extends Exception> implements Factory<E> {
        private final Class wrapped;
        private final Throwable throwable;

        static <E extends Exception> ExceptionFactory<E> newException(WithException<E> wrapped, Throwable throwable) {
            return new ExceptionFactory<E>(wrapped.getType(), throwable);
        }

        private ExceptionFactory(Class<E> wrapped, Throwable throwable) {
            this.wrapped = wrapped;
            this.throwable = throwable;
        }

        @Override
        public E create() throws FactoryException {
            try {
                Constructor<E> constructor = wrapped.getConstructor(Throwable.class);
                return constructor.newInstance(throwable);
            } catch (Exception e) {
                throw new FactoryException(e);
            }
        }
    }

}
