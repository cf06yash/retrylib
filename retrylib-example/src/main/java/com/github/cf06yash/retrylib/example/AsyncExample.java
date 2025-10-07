package com.github.cf06yash.retrylib.example;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import com.github.cf06yash.core.backoff.strategy.impl.JitterBackoffStrategy;
import com.github.cf06yash.core.retry.executor.RetryExecutorBuilder;
import com.github.cf06yash.core.retry.policy.RetryPolicy;
import com.github.cf06yash.core.retry.policy.RetryPolicyBuilder;
import com.github.cf06yash.core.retry.tagging.impl.DefaultRetryClassifier;
import com.github.cf06yash.retrylib.example.utils.TaskUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class AsyncExample {

    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void noFallbackExample() {
        RetryPolicy retryPolicy = RetryPolicyBuilder.newBuilder().classifier(DefaultRetryClassifier.INSTANCE).maxAttempts(2)
                                                    .build();
        BackoffStrategy backoffStrategy = JitterBackoffStrategy.withMinMaxDelay(1000, 3000);
        var executor = RetryExecutorBuilder.newBuilder().backoffStrategy(backoffStrategy).retryPolicy(retryPolicy)
                                           .scheduler(scheduler).buildAsync();
        try {
            var future = executor.executeAsync(TaskUtils.getSupplierTaskWithException());
            future.whenComplete((res, ex) -> {
                log.info("printing response no fallback " + res);
                log.info("printing exception message no fallback" + ex.getMessage());
            });
        } catch (Exception e) {
            log.info("error while completing task!!");
        }
    }

    public void fallbackExample() {
        RetryPolicy retryPolicy = RetryPolicyBuilder.newBuilder().classifier(DefaultRetryClassifier.INSTANCE).maxAttempts(2)
                                                    .build();
        BackoffStrategy backoffStrategy = JitterBackoffStrategy.withMinMaxDelay(1000, 3000);
        var executor = RetryExecutorBuilder.newBuilder().backoffStrategy(backoffStrategy).retryPolicy(retryPolicy)
                                           .scheduler(scheduler).buildAsync();
        try {
            var future = executor.executeAsyncWithFallback(TaskUtils.getSupplierTaskWithException(),
                    TaskUtils.getSupplierTask("this is fallback task"));
            future.whenComplete((res, ex) -> {
                log.info("printing response supplier " + res);
                log.info("printing exception message supplier " + ex.getMessage());
            });
        } catch (Exception e) {
            log.info("error while completing task!!");
        }
    }

    public void fallbackExampleWithRunnable() {
        RetryPolicy retryPolicy = RetryPolicyBuilder.newBuilder().classifier(DefaultRetryClassifier.INSTANCE).maxAttempts(2)
                                                    .build();
        BackoffStrategy backoffStrategy = JitterBackoffStrategy.withMinMaxDelay(1000, 3000);
        var executor = RetryExecutorBuilder.newBuilder().backoffStrategy(backoffStrategy).retryPolicy(retryPolicy)
                                           .scheduler(scheduler).buildAsync();
        try {
            var future = executor.executeAsyncWithFallback(TaskUtils.getSupplierTaskWithException(),
                    TaskUtils.getRunnableTask("this is fallback task from runnable", log));
            future.whenComplete((res, ex) -> {
                log.info("printing response runnable " + res);
                log.info("printing exception message runnable " + ex.getMessage());
            });
        } catch (Exception e) {
            log.info("error while completing task!!");
        }
    }
}
