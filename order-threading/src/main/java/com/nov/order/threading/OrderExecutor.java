package com.nov.order.threading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class OrderExecutor implements AutoCloseable {
    private final ExecutorService executor;

    public OrderExecutor(int threadCount) {
        executor = Executors.newFixedThreadPool(
                threadCount, Thread.ofPlatform().name("order-worker-", 1).factory());
    }

    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(task);
    }

    @Override
    public void close() {
        executor.close();
    }
}
