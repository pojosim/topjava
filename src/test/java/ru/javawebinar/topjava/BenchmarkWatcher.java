package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

public class BenchmarkWatcher extends TestWatcher {
    private static final Logger log = getLogger(BenchmarkWatcher.class);
    public static final StringBuilder RESULT_BUILDER = new StringBuilder();

    private static Date start;

    public static void showResult() {
        log.info(RESULT_BUILDER.toString());
    }

    @Override
    protected void starting(Description description) {
        start = new Date();
    }

    @Override
    protected void finished(Description description) {
        Long resultTime = new Date().getTime() - start.getTime();
        String result = String.format("Тест {%s}, время выполнения=%d мс", description.getMethodName(), resultTime);
        log.info(result);
        RESULT_BUILDER.append("\r\n").append(result);
    }

}
