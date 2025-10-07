package com.github.cf06yash.core.backoff.strategy.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import com.github.cf06yash.core.utils.ConcurrentTestHarness;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
class JitterBackoffStrategyTest {

    private static final long MAX_DELAY = 200;
    private static final long MIN_DELAY = 100;
    private static final int LIGHT_THREAD = 10;
    private static final int HEAVY_THREAD = 100;

    @Test
    void verifyJitterDelay() {
        BackoffStrategy strategy = JitterBackoffStrategy.withMinMaxDelay(MIN_DELAY,MAX_DELAY);
        for (int attempt = 1; attempt <= LIGHT_THREAD; attempt++) {
            long delay = strategy.computeDelay(attempt);
            Assertions.assertTrue(delay <= MAX_DELAY && delay >= MIN_DELAY);
        }
    }

    @Test
    void verifyExceptionOnNegativeMin() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> JitterBackoffStrategy.withMinMaxDelay(-1,MAX_DELAY));
    }

    @Test
    void verifyExceptionOnMinGreaterThanMax() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> JitterBackoffStrategy.withMinMaxDelay(MAX_DELAY,MIN_DELAY));
    }

    @Test
    void verifyExceptionOnLargeMaxValue() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> JitterBackoffStrategy.withMinMaxDelay(MIN_DELAY,Long.MAX_VALUE));
    }

    @Test
    void verifyExceptionOnNegativeAttempt() {
        BackoffStrategy strategy = JitterBackoffStrategy.withMinMaxDelay(MIN_DELAY,MAX_DELAY);
        Assertions.assertThrows(IllegalArgumentException.class, () -> strategy.computeDelay(-1));
    }

    @Test
    void shouldReturnJitterConcurrently()
            throws ExecutionException, InterruptedException {
        BackoffStrategy strategy = JitterBackoffStrategy.withMinMaxDelay(MIN_DELAY,MAX_DELAY);
        var utility = ConcurrentTestHarness.getInstance(LIGHT_THREAD);
        List<Long> delays = utility.runParallelWithReturn(() -> strategy.computeDelay(1));
        Assertions.assertEquals(LIGHT_THREAD, delays.size());
        for (var delay : delays) {
            Assertions.assertTrue(delay <= MAX_DELAY && delay >= MIN_DELAY);
        }
    }

    @Test
    void shouldReturnExponentialConcurrentlyHeavy()
            throws ExecutionException, InterruptedException {
        BackoffStrategy strategy = JitterBackoffStrategy.withMinMaxDelay(MIN_DELAY,MAX_DELAY);
        var utility = ConcurrentTestHarness.getInstance(HEAVY_THREAD);
        List<Long> delays = utility.runParallelWithReturn(() -> strategy.computeDelay(1));
        Assertions.assertEquals(HEAVY_THREAD, delays.size());
        for (var delay : delays) {
            Assertions.assertTrue(delay <= MAX_DELAY && delay >= MIN_DELAY);
        }
    }
}
