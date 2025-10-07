package com.github.cf06yash.core.retry.policy.impl;

import com.github.cf06yash.core.retry.policy.RetryPolicy;
import com.github.cf06yash.core.retry.tagging.RetryClassifier;
import com.github.cf06yash.core.retry.tagging.impl.DefaultRetryClassifier;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class DefaultRetryPolicy
        implements RetryPolicy {
    private final RetryClassifier retryClassifier;
    private final int maxAttempts;

    public DefaultRetryPolicy(RetryClassifier retryClassifier, int maxAttempts) {
        this.retryClassifier = retryClassifier;
        this.maxAttempts = maxAttempts;
    }

    public static DefaultRetryPolicy withMaxAttempts(int maxAttempts) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be positive");
        }
        return new DefaultRetryPolicy(DefaultRetryClassifier.INSTANCE, maxAttempts);
    }

    @Override
    public boolean shouldRetry(int attempts, Exception lastException) {
        return attempts < maxAttempts && retryClassifier.isRetryable(lastException);
    }

    public RetryClassifier getRetryClassifier() {
        return retryClassifier;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }
}
