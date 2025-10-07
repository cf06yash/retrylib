package com.github.cf06yash.retrylib.example;

import com.github.cf06yash.core.backoff.strategy.BackoffStrategy;
import com.github.cf06yash.core.backoff.strategy.impl.ExponentialBackoffStrategy;
import com.github.cf06yash.core.backoff.strategy.impl.FixedBackoffStrategy;
import com.github.cf06yash.core.backoff.strategy.impl.JitterBackoffStrategy;
import com.github.cf06yash.core.retry.executor.RetryExecutorBuilder;
import com.github.cf06yash.core.retry.policy.RetryPolicy;
import com.github.cf06yash.core.retry.policy.RetryPolicyBuilder;
import com.github.cf06yash.core.retry.policy.impl.DefaultRetryPolicy;
import com.github.cf06yash.core.retry.tagging.impl.DefaultRetryClassifier;
import com.github.cf06yash.retrylib.example.exception.CommonException;
import com.github.cf06yash.retrylib.example.utils.TaskUtils;

import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class SyncExample {

    private static final Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void exponentialExample() {
        RetryPolicy retryPolicy = DefaultRetryPolicy.withMaxAttempts(3);
        BackoffStrategy backoffStrategy = ExponentialBackoffStrategy.withBaseDelayMillis(1000);
        var executor = RetryExecutorBuilder.newBuilder().backoffStrategy(backoffStrategy).retryPolicy(retryPolicy).build();
        try {
            executor.execute(TaskUtils.getTaskWithException());
        } catch (Exception e) {
            log.info("error in processing task");
        }
    }

    public void fixedDelayExample() {
        RetryPolicy retryPolicy = DefaultRetryPolicy.withMaxAttempts(3);
        BackoffStrategy backoffStrategy = FixedBackoffStrategy.withDelayMillis(1000);
        var executor = RetryExecutorBuilder.newBuilder().backoffStrategy(backoffStrategy).retryPolicy(retryPolicy).build();
        try {
            executor.execute(TaskUtils.getTaskWithException());
        } catch (Exception e) {
            log.info("error in processing task");
        }
    }

    public void jitterDelayExample() {
        RetryPolicy retryPolicy = RetryPolicyBuilder.newBuilder().classifier(DefaultRetryClassifier.INSTANCE).maxAttempts(6)
                                                    .build();
        BackoffStrategy backoffStrategy = JitterBackoffStrategy.withMinMaxDelay(1000, 10000);
        var executor = RetryExecutorBuilder.newBuilder().backoffStrategy(backoffStrategy).retryPolicy(retryPolicy).build();
        try {
            executor.execute(TaskUtils.getTaskWithException());
        } catch (Exception e) {
            log.info("error in processing task");
        }
    }
}
