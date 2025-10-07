package com.github.cf06yash.core.backoff.strategy.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class NoDelayBackoffStrategy
        implements BackoffStrategy {

    private static final NoDelayBackoffStrategy INSTANCE = new NoDelayBackoffStrategy();

    private NoDelayBackoffStrategy() {
    }

    public static BackoffStrategy get() {
        return INSTANCE;
    }

    @Override
    public long computeDelay(int attemptCount) {
        if (attemptCount < 0) {
            throw new IllegalArgumentException("Attempt count cannot be negative");
        }
        return 0;
    }
}
