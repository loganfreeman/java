package com.scheng.guava;
import com.github.avarabyeu.wills.Will;
import com.github.avarabyeu.wills.WillExecutorService;
import com.github.avarabyeu.wills.WillExecutors;
import com.google.common.util.concurrent.MoreExecutors;
import com.smarttested.qa.smartassert.SmartAssert;
import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by scheng on 7/22/2015.
 */
public class WillExecutorsTest {

    @Test
    public void testGuavaDecorator() {
        WillExecutorService willExecutorService = WillExecutors.willDecorator(MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1)));
        DemoTask task = new DemoTask();
        Will<?> submit = willExecutorService.submit(task);

        /* waits for finish */
        submit.obtain();

        SmartAssert.assertHard(task.executed(), is(true), "Runnable is not executed");

    }

    @Test
    public void testJdkDecorator() {
        WillExecutorService willExecutorService = WillExecutors.willDecorator(Executors.newFixedThreadPool(1));
        DemoTask task = new DemoTask();
        Will<?> submit = willExecutorService.submit(task);

        /* waits for finish */
        submit.obtain();

        SmartAssert.assertHard(task.executed(), is(true), "Runnable is not executed");

    }


    private static class DemoTask implements Runnable {

        private AtomicInteger counter = new AtomicInteger();

        @Override
        public void run() {
            counter.incrementAndGet();
        }

        public AtomicInteger getCounter() {
            return counter;
        }

        public boolean executed() {
            return counter.get() > 0;
        }
    }
}