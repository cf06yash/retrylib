package com.github.cf06yash.core.retry.policy;

import com.github.cf06yash.core.retry.policy.impl.ConfigurableRetryPolicy;
import com.github.cf06yash.core.retry.tagging.RetryClassifier;
import com.github.cf06yash.core.retry.tagging.impl.DefaultRetryClassifier;

/**
 * Developed by cf06yash.
 * Created on 06-10-2025.
 */
public class RetryPolicyBuilder {

    private int maxAttempts = 3;
    private RetryClassifier retryClassifier;

    private RetryPolicyBuilder() {
    }

    public static RetryPolicyBuilder newBuilder(){
        return new RetryPolicyBuilder();
    }

    public RetryPolicyBuilder maxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
        return this;
    }

    public RetryPolicyBuilder classifier(RetryClassifier classifier) {
        this.retryClassifier = classifier;
        return this;
    }

    public RetryPolicy build() {
        RetryClassifier classifier = this.retryClassifier != null
                ? this.retryClassifier
                : DefaultRetryClassifier.INSTANCE;
        return ConfigurableRetryPolicy.of(classifier, this.maxAttempts);
    }
}
