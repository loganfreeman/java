package com.scheng.concurrency.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by scheng on 7/18/2015.
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface Concurrent {
    int count() default 5;
}