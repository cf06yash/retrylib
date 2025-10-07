package com.github.cf06yash.core.backoff.strategy.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;

import java.util.concurrent.TimeUnit;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class ExponentialBackoffStrategy
        implements BackoffStrategy {

    private static final long MAX_DELAY_MILLIS = TimeUnit.HOURS.toMillis(1);
    private final long baseDelayMillis;

    private ExponentialBackoffStrategy(long baseDelayMillis) {
        this.baseDelayMillis = baseDelayMillis;
    }

    public static BackoffStrategy withBaseDelayMillis(long baseDelayMillis) {
        if (baseDelayMillis <= 0) {
            throw new IllegalArgumentException("Base delay must be positive");
        }
        return new ExponentialBackoffStrategy(baseDelayMillis);
    }

    @Override
    public long computeDelay(int attemptCount) {
        if (attemptCount < 0) {
            throw new IllegalArgumentException("Attempt count cannot be negative");
        }
        if (attemptCount >= 63) {
            return MAX_DELAY_MILLIS;
        }
        long multiplier = 1L << attemptCount;
        if (baseDelayMillis > Long.MAX_VALUE / multiplier) {
            return MAX_DELAY_MILLIS;
        }
        long delay = baseDelayMillis * multiplier;
        return Math.min(delay, MAX_DELAY_MILLIS);
    }
}
