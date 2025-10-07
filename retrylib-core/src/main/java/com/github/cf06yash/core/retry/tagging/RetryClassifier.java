package com.github.cf06yash.core.retry.tagging;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public interface RetryClassifier {

    boolean isRetryable(Exception e);
}
