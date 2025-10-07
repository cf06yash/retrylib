package com.github.cf06yash.core.backoff.strategy.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class JitterBackoffStrategy
        implements BackoffStrategy {

    private final long minDelayMillis;
    private final long maxDelayMillis;

    private JitterBackoffStrategy(long minDelayMillis, long maxDelayMillis) {
        this.minDelayMillis = minDelayMillis;
        this.maxDelayMillis = maxDelayMillis;
    }

    public static BackoffStrategy withMinMaxDelay(long minDelayMillis, long maxDelayMillis) {
        if (minDelayMillis < 0 || minDelayMillis > maxDelayMillis) {
            throw new IllegalArgumentException("Invalid jitter range");
        }
        if (maxDelayMillis == Long.MAX_VALUE) {
            throw new IllegalArgumentException("Max delay too large; must be < Long.MAX_VALUE");
        }
        return new JitterBackoffStrategy(minDelayMillis, maxDelayMillis);
    }

    @Override
    public long computeDelay(int attemptCount) {
        if (attemptCount < 0) {
            throw new IllegalArgumentException("Attempt count cannot be negative");
        }
        return ThreadLocalRandom.current().nextLong(minDelayMillis, maxDelayMillis + 1);
    }
}
