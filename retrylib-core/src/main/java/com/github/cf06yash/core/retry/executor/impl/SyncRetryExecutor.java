package com.github.cf06yash.core.retry.executor.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import com.github.cf06yash.core.retry.executor.IRetryExecutor;
import com.github.cf06yash.core.retry.policy.RetryPolicy;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class SyncRetryExecutor
        implements IRetryExecutor {

    private static final Logger log = Logger.getLogger(SyncRetryExecutor.class.getName());

    private final RetryPolicy retryPolicy;
    private final BackoffStrategy backoffStrategy;

    public SyncRetryExecutor(RetryPolicy retryPolicy, BackoffStrategy backoffStrategy) {
        this.retryPolicy = retryPolicy;
        this.backoffStrategy = backoffStrategy;
    }

    @Override
    public <T> T execute(Callable<T> task)
            throws Exception {
        return executeInternal(task, e -> {
            throw e;
        });
    }

    @Override
    public <T> T executeWithFallback(Callable<T> task, Supplier<T> fallback)
            throws Exception {
        return executeInternal(task, e -> fallback.get());
    }

    @Override
    public <T> T executeWithFallback(Callable<T> task, Runnable fallback)
            throws Exception {
        return executeInternal(task, e -> {
            fallback.run();
            throw e;
        });
    }

    private <T> T executeInternal(Callable<T> task, FallbackHandler<T> fallback)
            throws Exception {
        int attempt = 1;
        while (true) {
            try {
                log.info("Inside execute internal at " + new Date() + " attempt: " + attempt);
                return task.call();
            } catch (Exception e) {
                if (!retryPolicy.shouldRetry(attempt, e)) {
                    return fallback.handle(e);
                }
                try{
                    Thread.sleep(backoffStrategy.computeDelay(attempt));
                } catch (InterruptedException ix){
                    Thread.currentThread().interrupt();
                    throw ix;
                }
                attempt++;
            }
        }
    }

    @FunctionalInterface
    private interface FallbackHandler<T> {
        T handle(Exception e)
                throws Exception;
    }
}
