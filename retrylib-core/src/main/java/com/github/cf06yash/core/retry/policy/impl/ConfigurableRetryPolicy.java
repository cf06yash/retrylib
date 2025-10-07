package com.github.cf06yash.core.retry.policy.impl;

import com.github.cf06yash.core.retry.policy.RetryPolicy;
import com.github.cf06yash.core.retry.tagging.RetryClassifier;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class ConfigurableRetryPolicy
        implements RetryPolicy {

    private final RetryClassifier retryClassifier;
    private final int maxAttempts;

    ConfigurableRetryPolicy(RetryClassifier retryClassifier, int maxAttempts) {
        this.retryClassifier = retryClassifier;
        this.maxAttempts = maxAttempts;
    }

    public static RetryPolicy of(RetryClassifier classifier, int maxAttempts) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be positive");
        }
        if (classifier == null) {
            throw new IllegalArgumentException("retry classifier cannot be null");
        }
        return new ConfigurableRetryPolicy(classifier, maxAttempts);
    }

    public RetryClassifier getRetryClassifier() {
        return retryClassifier;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    @Override
    public boolean shouldRetry(int attempts, Exception lastException) {
        return attempts < maxAttempts && retryClassifier.isRetryable(lastException);
    }
}
