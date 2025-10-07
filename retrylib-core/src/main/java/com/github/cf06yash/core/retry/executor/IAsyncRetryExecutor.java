package com.github.cf06yash.core.retry.executor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public interface IAsyncRetryExecutor {
    <T> CompletableFuture<T> executeAsync(Supplier<T> task);

    <T> CompletableFuture<T> executeAsyncWithFallback(Supplier<T> task, Supplier<T> fallback);

    <T> CompletableFuture<T> executeAsyncWithFallback(Supplier<T> task, Runnable fallback);
}
