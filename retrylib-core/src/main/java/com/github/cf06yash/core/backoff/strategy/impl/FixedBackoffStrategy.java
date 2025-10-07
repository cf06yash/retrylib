package com.github.cf06yash.core.backoff.strategy.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class FixedBackoffStrategy
        implements BackoffStrategy {

    private final long fixedDelayMillis;

    private FixedBackoffStrategy(long fixedDelayMillis) {
        this.fixedDelayMillis = fixedDelayMillis;
    }

    public static BackoffStrategy withDelayMillis(long fixedDelayMillis) {
        if (fixedDelayMillis <= 0){
            throw new IllegalArgumentException("fixed delay must be positive");
        }
        return new FixedBackoffStrategy(fixedDelayMillis);
    }

    @Override
    public long computeDelay(int attemptCount) {
        if (attemptCount < 0) {
            throw new IllegalArgumentException("Attempt count cannot be negative");
        }
        return this.fixedDelayMillis;
    }
}
