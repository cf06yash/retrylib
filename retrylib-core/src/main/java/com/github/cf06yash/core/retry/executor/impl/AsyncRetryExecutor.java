package com.github.cf06yash.core.retry.executor.impl;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import com.github.cf06yash.core.retry.executor.IAsyncRetryExecutor;
import com.github.cf06yash.core.retry.policy.RetryPolicy;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class AsyncRetryExecutor
        implements IAsyncRetryExecutor {

    private static final Logger log = Logger.getLogger(AsyncRetryExecutor.class.getName());

    private final RetryPolicy retryPolicy;
    private final BackoffStrategy backoffStrategy;
    private final ScheduledExecutorService scheduler;

    public AsyncRetryExecutor(RetryPolicy retryPolicy, BackoffStrategy backoffStrategy, ScheduledExecutorService scheduler) {
        this.retryPolicy = retryPolicy;
        this.backoffStrategy = backoffStrategy;
        this.scheduler = scheduler;
    }

    @Override
    public <T> CompletableFuture<T> executeAsync(Supplier<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executeInternal(task, future, 1, future::completeExceptionally);
        return future;
    }

    @Override
    public <T> CompletableFuture<T> executeAsyncWithFallback(Supplier<T> task, Supplier<T> fallback) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executeInternal(task, future, 1, e -> future.complete(fallback.get()));
        return future;
    }

    @Override
    public <T> CompletableFuture<T> executeAsyncWithFallback(Supplier<T> task, Runnable fallback) {
        CompletableFuture<T> future = new CompletableFuture<>();
        executeInternal(task, future, 1, e -> {
            fallback.run();
            future.completeExceptionally(e);
        });
        return future;
    }

    private <T> void executeInternal(Supplier<T> task, CompletableFuture<T> future, int attempt, AsyncFallbackHandler fallback) {
        scheduler.execute(() -> {
            try {
                var t = task.get();
                future.complete(t);
            } catch (Exception e) {
                if (!retryPolicy.shouldRetry(attempt, e)) {
                    try {
                        fallback.handle(e);
                    } catch (Exception fallbackEx) {
                        future.completeExceptionally(fallbackEx);
                    }
                    return;
                }
                long delay = backoffStrategy.computeDelay(attempt);
                log.info("calculated delay async executor: " + delay);
                scheduler.schedule(() -> executeInternal(task, future, attempt + 1, fallback), delay, TimeUnit.MILLISECONDS);
            }
        });
    }

    @FunctionalInterface
    private interface AsyncFallbackHandler {
        void handle(Exception e);
    }
}
