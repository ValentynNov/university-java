package com.nov.order.storage.internal;

import com.nov.order.model.ProcessedOrder;
import com.nov.order.model.Product;
import com.nov.order.storage.OrderRepository;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class InMemoryOrderRepository<T extends Product> implements OrderRepository<T> {
    private final ConcurrentMap<UUID, ProcessedOrder<T>> orders = new ConcurrentHashMap<>();

    @Override
    public boolean save(ProcessedOrder<T> order) {
        return orders.putIfAbsent(order.order().id(), order) == null;
    }

    @Override
    public List<ProcessedOrder<T>> findAll() {
        return List.copyOf(orders.values());
    }
}
