package com.github.cf06yash.core.retry.policy;

import com.github.cf06yash.core.retry.tagging.RetryClassifier;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public interface RetryPolicy {
    boolean shouldRetry(int attempts, Exception lastException);
    RetryClassifier getRetryClassifier();
    int getMaxAttempts();
}
