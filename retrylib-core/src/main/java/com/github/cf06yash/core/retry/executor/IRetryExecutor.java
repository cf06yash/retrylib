package com.github.cf06yash.core.retry.executor;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public interface IRetryExecutor {

    <T> T execute(Callable<T> task)
            throws Exception;

    <T> T executeWithFallback(Callable<T> task, Supplier<T> fallback)
            throws Exception;

    <T> T executeWithFallback(Callable<T> task, Runnable fallback)
            throws Exception;
}
