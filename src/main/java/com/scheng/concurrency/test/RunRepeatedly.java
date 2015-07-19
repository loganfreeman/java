package com.scheng.concurrency.test;

/**
 * Created by scheng on 7/18/2015.
 */
import com.scheng.concurrency.annotation.Repeating;
import junit.framework.AssertionFailedError;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

class RunRepeatedly extends Statement {

    private final FrameworkMethod method;
    private final Statement statement;

    RunRepeatedly(FrameworkMethod method, Statement statement) {
        this.method = method;
        this.statement = statement;
    }

    public void evaluate() throws Throwable {
        if (repeating(method))
            for (int i = 0; i < repetition(method); i++) {
                try {
                    statement.evaluate();
                } catch (AssertionFailedError e) {
                    throw new AssertionFailedError(String.format("%s (failed after %d successful attempts)", e.getMessage(), i));
                }
            }
        else
            statement.evaluate();
    }

    private static boolean repeating(FrameworkMethod method) {
        return method.getAnnotation(Repeating.class) != null;
    }

    private static int repetition(FrameworkMethod method) {
        return method.getAnnotation(Repeating.class).repetition();
    }
}