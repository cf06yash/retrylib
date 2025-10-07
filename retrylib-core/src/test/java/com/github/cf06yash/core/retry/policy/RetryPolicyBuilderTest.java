package com.github.cf06yash.core.retry.policy;

import com.github.cf06yash.core.retry.policy.impl.ConfigurableRetryPolicy;
import com.github.cf06yash.core.retry.policy.impl.DefaultRetryPolicy;
import com.github.cf06yash.core.retry.tagging.impl.DefaultRetryClassifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
class RetryPolicyBuilderTest {

    private static final int DEFAULT_MAX_ATTEMPTS = 3;
    private static final int CUSTOM_MAX_ATTEMPTS = 7;

    @Test
    void verifyBuildWithDefaultValues() {
        var policy = RetryPolicyBuilder.newBuilder().build();
        Assertions.assertInstanceOf(ConfigurableRetryPolicy.class, policy);
        Assertions.assertEquals(DEFAULT_MAX_ATTEMPTS, policy.getMaxAttempts());
        Assertions.assertInstanceOf(DefaultRetryClassifier.class, policy.getRetryClassifier());
    }

    @Test
    void verifyWithCustomValues() {
        var policy = RetryPolicyBuilder.newBuilder().classifier(DefaultRetryClassifier.INSTANCE).maxAttempts(CUSTOM_MAX_ATTEMPTS)
                                       .build();
        Assertions.assertEquals(CUSTOM_MAX_ATTEMPTS, policy.getMaxAttempts());
        Assertions.assertInstanceOf(DefaultRetryClassifier.class, policy.getRetryClassifier());
    }
}
