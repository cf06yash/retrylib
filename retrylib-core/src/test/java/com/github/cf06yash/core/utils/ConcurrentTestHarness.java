package com.github.cf06yash.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
public class ConcurrentTestHarness {

    private final CountDownLatch readyLatch;
    private final CountDownLatch startLatch;
    private final CountDownLatch doneLatch;
    private final ExecutorService executor;
    private final int threadCount;

    private ConcurrentTestHarness(CountDownLatch readyLatch, CountDownLatch startLatch, CountDownLatch doneLatch,
                                 ExecutorService executor, int threadCount) {
        this.readyLatch = readyLatch;
        this.startLatch = startLatch;
        this.doneLatch = doneLatch;
        this.executor = executor;
        this.threadCount = threadCount;
    }

    public static ConcurrentTestHarness getInstance(int threads) {
        return new ConcurrentTestHarness(new CountDownLatch(threads), new CountDownLatch(1), new CountDownLatch(threads),
                Executors.newFixedThreadPool(threads), threads);
    }

    public void runParallel(Runnable task)
            throws InterruptedException {
        for (int i = 0; i < this.threadCount; i++) {
            this.executor.submit(() -> {
                try {
                    this.readyLatch.countDown();
                    this.startLatch.await();
                    task.run();
                } catch (InterruptedException ignored) {
                } finally {
                    this.doneLatch.countDown();
                }
            });
        }
        this.readyLatch.await();
        this.startLatch.countDown();
        this.doneLatch.await();
        this.executor.shutdownNow();
    }

    public <T> List<T> runParallelWithReturn(Callable<T> task)
            throws InterruptedException, ExecutionException {
        List<Future<T>> futures = new ArrayList<>(threadCount);
        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                try {
                    readyLatch.countDown();
                    startLatch.await();
                    return task.call();
                } finally {
                    doneLatch.countDown();
                }
            }));
        }
        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        List<T> results = new ArrayList<>();
        for (var future : futures) {
            results.add(future.get());
        }
        return results;
    }
}
