package com.scheng.concurrency.annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by scheng on 7/18/2015.
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
/**
 * Based on the annotation of the same name by Brian Goetz and Tim Tim Peierls.
 * <p/>
 * Indicates that a class is intended to be immutable. Can be combined with the {@link DeclareImmutableError} AspectJ
 * aspect to force compilation errors for basic mutator methods on @Immutable classes.
 * <p/>
 */
public @interface Immutable {
}