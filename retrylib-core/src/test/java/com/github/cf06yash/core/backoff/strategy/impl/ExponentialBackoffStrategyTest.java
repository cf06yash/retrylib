package com.github.cf06yash.core.backoff.strategy.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import com.github.cf06yash.core.utils.ConcurrentTestHarness;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
class ExponentialBackoffStrategyTest {

    private static final long BASE_DELAY = 100;
    private static final int LIGHT_THREAD = 10;
    private static final int HEAVY_THREAD = 100;

    @Test
    void verifyExponentialDelay() {
        BackoffStrategy strategy = ExponentialBackoffStrategy.withBaseDelayMillis(BASE_DELAY);
        for (int attempt = 1; attempt <= LIGHT_THREAD; attempt++) {
            long delay = strategy.computeDelay(attempt);
            Assertions.assertEquals(BASE_DELAY * (1L << attempt), delay);
        }
    }

    @Test
    void verifyExceptionOnNegativeDelay() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> ExponentialBackoffStrategy.withBaseDelayMillis(-10));
    }

    @Test
    void verifyExceptionOnNegativeAttempt() {
        BackoffStrategy strategy = ExponentialBackoffStrategy.withBaseDelayMillis(BASE_DELAY);
        Assertions.assertThrows(IllegalArgumentException.class, () -> strategy.computeDelay(-1));
    }

    @Test
    void shouldCapWhenAttemptCountIsTooLarge() {
        BackoffStrategy strategy = ExponentialBackoffStrategy.withBaseDelayMillis(BASE_DELAY);
        Assertions.assertEquals(TimeUnit.HOURS.toMillis(1), strategy.computeDelay(100));
    }

    @Test
    void shouldCapWhenComputedDelayWouldOverflow() {
        BackoffStrategy strategy = ExponentialBackoffStrategy.withBaseDelayMillis(BASE_DELAY);
        Assertions.assertEquals(TimeUnit.HOURS.toMillis(1), strategy.computeDelay(62));
    }

    @Test
    void shouldReturnExponentialConcurrently()
            throws ExecutionException, InterruptedException {
        BackoffStrategy strategy = ExponentialBackoffStrategy.withBaseDelayMillis(BASE_DELAY);
        var utility = ConcurrentTestHarness.getInstance(LIGHT_THREAD);
        List<Long> delays = utility.runParallelWithReturn(() -> strategy.computeDelay(1));
        Assertions.assertEquals(LIGHT_THREAD, delays.size());
        for (var delay : delays) {
            Assertions.assertEquals(BASE_DELAY * (1L << 1), delay);
        }
    }

    @Test
    void shouldReturnExponentialConcurrentlyHeavy()
            throws ExecutionException, InterruptedException {
        BackoffStrategy strategy = ExponentialBackoffStrategy.withBaseDelayMillis(BASE_DELAY);
        var utility = ConcurrentTestHarness.getInstance(HEAVY_THREAD);
        List<Long> delays = utility.runParallelWithReturn(() -> strategy.computeDelay(1));
        Assertions.assertEquals(HEAVY_THREAD, delays.size());
        for (var delay : delays) {
            Assertions.assertEquals(BASE_DELAY * (1L << 1), delay);
        }
    }
}
