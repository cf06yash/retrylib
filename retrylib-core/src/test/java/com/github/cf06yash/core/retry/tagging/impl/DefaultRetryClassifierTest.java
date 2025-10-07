package com.github.cf06yash.core.retry.tagging.impl;

import com.github.cf06yash.core.retry.tagging.RetryClassifier;
import com.github.cf06yash.core.utils.ExceptionTestUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
class DefaultRetryClassifierTest {

    private final RetryClassifier retryClassifier = DefaultRetryClassifier.INSTANCE;

    @Test
    void shouldReturnTrueForRetryableException() {
        Assertions.assertTrue(retryClassifier.isRetryable(ExceptionTestUtils.getRetryableException()));
    }

    @Test
    void shouldReturnFalseForNonRetryableException() {
        Assertions.assertFalse(retryClassifier.isRetryable(ExceptionTestUtils.getNonRetryableException()));
    }

    @Test
    void shouldReturnFalseForNullArgument() {
        Assertions.assertFalse(retryClassifier.isRetryable(null));
    }
}
