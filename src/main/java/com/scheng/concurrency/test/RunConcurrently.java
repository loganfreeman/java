package com.scheng.concurrency.test;

/**
 * Created by scheng on 7/18/2015.
 */

import com.scheng.concurrency.annotation.Concurrent;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.util.concurrent.Callable;
import java.util.concurrent.*;

class RunConcurrently extends Statement {

    private final FrameworkMethod method;
    private final Statement statement;

    RunConcurrently(FrameworkMethod method, Statement statement) {
        this.method = method;
        this.statement = statement;
    }

    public void evaluate() throws Throwable {
        if (concurrent(method)) {
            ExecutorCompletionService<Void> service = createCompletionService();
            startThreads(service);
            Throwable throwable = waitFor(service);
            if (throwable != null)
                throw throwable;
        } else
            statement.evaluate();
    }

    private ExecutorCompletionService createCompletionService() {
        return new ExecutorCompletionService(new Executor() {
            private int count;

            public void execute(Runnable runnable) {
                new Thread(runnable, method.getName() + "-Thread-" + count++).start();
            }
        });
    }

    private void startThreads(ExecutorCompletionService<Void> service) {
        CountDownLatch start = new CountDownLatch(1);
        for (int i = 0; i < threadCount(method); i++)
            service.submit(new StatementEvaluatingTask(statement, start));
        start.countDown();
    }

    private Throwable waitFor(ExecutorCompletionService<Void> service) {
        Throwable throwable = null;
        for (int i = 0; i < threadCount(method); i++) {
            try {
                service.take().get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                throwable = e.getCause();
                break;
            }
        }
        return throwable;
    }

    private static boolean concurrent(FrameworkMethod method) {
        return method.getAnnotation(Concurrent.class) != null;
    }

    private static int threadCount(FrameworkMethod method) {
        return method.getAnnotation(Concurrent.class).count();
    }

    private static class StatementEvaluatingTask implements Callable<Void> {
        private final Statement statement;
        private final CountDownLatch start;

        public StatementEvaluatingTask(Statement statement, CountDownLatch start) {
            this.statement = statement;
            this.start = start;
        }

        public Void call() throws Exception {
            try {
                start.await();
                statement.evaluate();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
            return null;
        }
    }
}