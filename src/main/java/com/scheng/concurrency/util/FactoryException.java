package com.scheng.concurrency.util;

/**
 * Created by scheng on 7/18/2015.
 */
public class FactoryException extends RuntimeException {
    public FactoryException() {
    }

    public FactoryException(String message) {
        super(message);
    }

    public FactoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public FactoryException(Throwable cause) {
        super(cause);
    }
}