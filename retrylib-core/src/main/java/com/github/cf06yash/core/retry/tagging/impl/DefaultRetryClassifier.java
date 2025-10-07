package com.github.cf06yash.core.retry.tagging.impl;

import com.github.cf06yash.core.retry.tagging.RetryClassifier;
import com.github.cf06yash.core.retry.tagging.RetryableException;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class DefaultRetryClassifier
        implements RetryClassifier {

    public static final DefaultRetryClassifier INSTANCE = new DefaultRetryClassifier();

    private DefaultRetryClassifier(){}

    @Override
    public boolean isRetryable(Exception e) {
        if (e == null){
            return false;
        }
        return e instanceof RetryableException;
    }
}
