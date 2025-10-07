package com.github.cf06yash.core.utils;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
public class ExceptionTestUtils {

    public static Exception getRetryableException(){
        return new RetryableExceptionTest();
    }

    public static Exception getNonRetryableException(){
        return new NonRetryableExceptionTest();
    }
}
