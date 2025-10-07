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
class FixedBackoffStrategyTest {

    private static final long FIXED_DELAY = 500;
    private static final int LIGHT_THREAD = 10;
    private static final int HEAVY_THREAD = 100;

    @Test
    void verifyFixedDelayAcross() {
        BackoffStrategy strategy = FixedBackoffStrategy.withDelayMillis(FIXED_DELAY);
        for (int attempt = 1; attempt <= LIGHT_THREAD; attempt++) {
            long delay = strategy.computeDelay(attempt);
            Assertions.assertEquals(FIXED_DELAY, delay);
        }
    }

    @Test
    void verifyExceptionOnNegativeDelay() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> FixedBackoffStrategy.withDelayMillis(-10));
    }

    @Test
    void verifyExceptionOnNegativeAttempt() {
        BackoffStrategy strategy = FixedBackoffStrategy.withDelayMillis(FIXED_DELAY);
        Assertions.assertThrows(IllegalArgumentException.class, () -> strategy.computeDelay(-1));
    }

    @Test
    void shouldReturnFixedDelayConcurrently()
            throws InterruptedException, ExecutionException {
        BackoffStrategy strategy = FixedBackoffStrategy.withDelayMillis(FIXED_DELAY);
        var utility = ConcurrentTestHarness.getInstance(LIGHT_THREAD);
        List<Long> delays = utility.runParallelWithReturn(() -> strategy.computeDelay(1));
        Assertions.assertEquals(LIGHT_THREAD, delays.size());
        for (var delay : delays) {
            Assertions.assertEquals(FIXED_DELAY, delay);
        }
    }

    @Test
    void shouldReturnFixedDelayConcurrentlyHeavy()
            throws InterruptedException, ExecutionException {
        BackoffStrategy strategy = FixedBackoffStrategy.withDelayMillis(FIXED_DELAY);
        var utility = ConcurrentTestHarness.getInstance(HEAVY_THREAD);
        List<Long> delays = utility.runParallelWithReturn(() -> strategy.computeDelay(1));
        Assertions.assertEquals(HEAVY_THREAD, delays.size());
        for (var delay : delays) {
            Assertions.assertEquals(FIXED_DELAY, delay);
        }
    }
}
