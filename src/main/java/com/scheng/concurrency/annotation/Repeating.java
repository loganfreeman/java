package com.scheng.concurrency.annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by scheng on 7/18/2015.
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Repeating {
    public abstract int repetition() default 100;
}
