package com.github.cf06yash.core.backoff.strategy.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
class NoDelayBackoffStrategyTest {

    @Test
    void verifyZeroDelay(){
        BackoffStrategy strategy = NoDelayBackoffStrategy.get();
        for (int attempt = 0; attempt < 10; attempt++) {
            long delay = strategy.computeDelay(attempt);
            Assertions.assertEquals(0L,delay);
        }
    }

    @Test
    void verifyExceptionNegativeAttempt(){
        BackoffStrategy strategy = NoDelayBackoffStrategy.get();
        Assertions.assertThrows(IllegalArgumentException.class, () -> strategy.computeDelay(-1));
    }
}
