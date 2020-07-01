package ru.javawebinar.topjava;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class BenchmarkWatcher extends TestWatcher {
    private static final Logger log = getLogger(BenchmarkWatcher.class);

    public final StringBuilder resultBuilder;

    public BenchmarkWatcher() {
        resultBuilder = new StringBuilder();
        resultBuilder.append("\r\n")
                .append(String.format("%-28s %s", "test", "time"));
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long start = now();
                base.evaluate();
                long spent = TimeUnit.NANOSECONDS.toMillis(now() - start);
                log.info(String.format("Test [%s] spent %d ms", description.getMethodName(), spent));
                resultBuilder.append("\r\n").append(String.format("%-28s %d ms", description.getMethodName(), spent));
            }
        };
    }

    private long now() {
        return System.nanoTime();
    }

    public void showResult() {
        log.info(resultBuilder.toString());
    }

}
