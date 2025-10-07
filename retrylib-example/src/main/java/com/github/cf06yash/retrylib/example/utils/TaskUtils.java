package com.github.cf06yash.retrylib.example.utils;

import com.github.cf06yash.retrylib.example.exception.CommonException;

import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Developed by cf06yash.
 * Created on 07-10-2025.
 */
public class TaskUtils {

    public static Callable<?> getTaskWithException() {
        return () -> {
            throw new CommonException(String.valueOf(System.currentTimeMillis()));
        };
    }

    public static Callable<?> getSuccessfulTask() {
        return () -> {
            Thread.sleep(500);
            return "yay";
        };
    }

    public static Supplier<String> getSupplierTaskWithException(){
        return () -> {
            throw new CommonException("supplier exception");
        };
    }

    public static Supplier<String> getSupplierTask(final String message){
        return () -> message;
    }

    public static Runnable getRunnableTask(String message, Logger logger) {
        return () ->  logger.info(message);
    }
}
