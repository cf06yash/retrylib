package com.github.cf06yash.core.backoff.strategy;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public interface BackoffStrategy {
    long computeDelay(int attemptCount);
}
