package com.github.cf06yash.retrylib.example.exception;

import com.github.cf06yash.core.retry.tagging.RetryableException;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
public class CommonException extends RuntimeException implements RetryableException {

    public CommonException(String message) {
        super(message);
    }
}
