package com.github.cf06yash.retrylib.example;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
public class main {

    public static void main(String[] args){
        SyncExample syncExample = new SyncExample();
        syncExample.exponentialExample();
        syncExample.fixedDelayExample();
        syncExample.jitterDelayExample();

        AsyncExample asyncExample = new AsyncExample();
        asyncExample.noFallbackExample();
        asyncExample.fallbackExample();
        asyncExample.fallbackExampleWithRunnable();
    }
}
