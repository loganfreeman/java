package com.scheng.concurrency.util;

/**
 * Created by scheng on 7/18/2015.
 */
import java.io.IOException;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * Detect Java-level deadlocks both intrinsic and {@link java.util.concurrent.locks.Lock} based.
 */
public class DeadlockDetector {

    private static final ThreadMXBean MBEAN = ManagementFactory.getThreadMXBean();
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Print deadlocks without deadlock stack traces.
     * @param writer an {@link OutputStream} writer
     */
    public static void printDeadlocks(OutputStream writer) {
        printDeadlocks(writer, 0);
    }

    /**
     * Print deadlocks with deadlock stack traces.
     * @param writer an {@link OutputStream} writer
     * @param stackDepth the stack depth to log (0 for no stack)
     */
    public static void printDeadlocks(OutputStream writer, int stackDepth) {
        List<ThreadInfo> deadlocks = findDeadlocks(stackDepth);
        if (deadlocks.isEmpty())
            return;
        print(writer, deadlocks);
    }

    private static void print(OutputStream writer, List<ThreadInfo> deadlocks) {
        try {
            writeln(writer, "Deadlock detected");
            writeln(writer, "=================");
            for (ThreadInfo thread : deadlocks) {
                writeln(writer, format("%s\"%s\":", LINE_SEPARATOR, thread.getThreadName()));
                writeln(writer, format("  waiting to lock Monitor of %s ", thread.getLockName()));
                writeln(writer, format("  which is held by \"%s\"", thread.getLockOwnerName()));
                ThreadDump.printStackTrace(writer, thread.getStackTrace());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeln(OutputStream writer, String string) throws IOException {
        writer.write(format("%s%s", string, LINE_SEPARATOR).getBytes());
    }

    private static List<ThreadInfo> findDeadlocks(int stackDepth) {
        long[] result;
        if (MBEAN.isSynchronizerUsageSupported())
            result = MBEAN.findDeadlockedThreads();
        else
            result = MBEAN.findMonitorDeadlockedThreads();
        long[] monitorDeadlockedThreads = result;
        if (monitorDeadlockedThreads == null)
            return emptyList();
        return asList(MBEAN.getThreadInfo(monitorDeadlockedThreads, stackDepth));
    }

}