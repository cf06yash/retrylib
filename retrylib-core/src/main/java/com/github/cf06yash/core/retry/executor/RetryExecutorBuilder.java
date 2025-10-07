package com.github.cf06yash.core.retry.executor;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import com.github.cf06yash.core.retry.executor.impl.AsyncRetryExecutor;
import com.github.cf06yash.core.retry.executor.impl.SyncRetryExecutor;
import com.github.cf06yash.core.retry.policy.RetryPolicy;

import java.util.concurrent.ScheduledExecutorService;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class RetryExecutorBuilder {
    private RetryPolicy retryPolicy;
    private BackoffStrategy backoffStrategy;
    private ScheduledExecutorService scheduler;

    private RetryExecutorBuilder() {
    }

    public static RetryExecutorBuilder newBuilder() {
        return new RetryExecutorBuilder();
    }

    public RetryExecutorBuilder retryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return this;
    }

    public RetryExecutorBuilder backoffStrategy(BackoffStrategy backoffStrategy) {
        this.backoffStrategy = backoffStrategy;
        return this;
    }

    public RetryExecutorBuilder scheduler(ScheduledExecutorService scheduler) {
        this.scheduler = scheduler;
        return this;
    }

    public IRetryExecutor build() {
        if (retryPolicy == null || backoffStrategy == null) {
            throw new IllegalStateException("RetryPolicy and BackoffStrategy must be set");
        }
        return new SyncRetryExecutor(retryPolicy, backoffStrategy);
    }

    public IAsyncRetryExecutor buildAsync() {
        if (retryPolicy == null || backoffStrategy == null || scheduler == null) {
            throw new IllegalStateException("RetryPolicy, BackoffStrategy, and Scheduler must be set for async");
        }
        return new AsyncRetryExecutor(retryPolicy, backoffStrategy, scheduler);
    }
}
