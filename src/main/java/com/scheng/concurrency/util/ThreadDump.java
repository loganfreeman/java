package com.scheng.concurrency.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.Callable;
import static java.lang.String.format;

/**
 * Created by scheng on 7/18/2015.
 */
public class ThreadDump {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static void dumpThreads(OutputStream stream) {
        DeadlockDetector.printDeadlocks(stream);
        ExceptionWrapper.wrapAsRuntimeException(printThreadDump(stream));
    }

    private static Callable<Void> printThreadDump(final OutputStream writer) {
        return new Callable<Void>() {
            @Override
            public Void call() throws IOException {
                Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();
                for (Thread thread : stackTraces.keySet())
                    print(thread, stackTraces.get(thread));
                return null;
            }

            private void print(Thread thread, StackTraceElement[] stackTraceElements) throws IOException {
                writeln(writer, format("%sThread %s@%d: (state = %s)", LINE_SEPARATOR, thread.getName(), thread.getId(), thread.getState()));
                printStackTrace (writer, stackTraceElements);
            }
        };
    }

    static void printStackTrace (final OutputStream writer, StackTraceElement[] stackTraceElements) throws IOException {
        for (StackTraceElement stackTraceElement : stackTraceElements)
            writeln(writer, format(" - %s", stackTraceElement.toString()));
    }

    private static void writeln(OutputStream writer, String string) throws IOException {
        writer.write(format("%s%s", string, LINE_SEPARATOR).getBytes());
    }

}