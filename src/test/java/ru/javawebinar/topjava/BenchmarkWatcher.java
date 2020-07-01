package ru.javawebinar.topjava;

import org.junit.AssumptionViolatedException;
import org.junit.rules.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class BenchmarkWatcher extends Stopwatch {
    private static final Logger log = getLogger(BenchmarkWatcher.class);
    public static final StringBuilder RESULT_BUILDER = new StringBuilder();

    public static void showResult() {
        log.info(RESULT_BUILDER.toString());
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, "succeeded", nanos);
    }

    @Override
    protected void failed(long nanos, Throwable e, Description description) {
        logInfo(description, "failed", nanos);
    }

    @Override
    protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
        logInfo(description, "skipped", nanos);
    }

    private void logInfo(Description description, String status, long nanos) {
        String result = String.format("Test {%s} %s, spent %d ms", description.getMethodName(),
                status, TimeUnit.NANOSECONDS.toMillis(nanos));
        log.info(result);
        RESULT_BUILDER.append("\r\n").append(result);
    }

}
