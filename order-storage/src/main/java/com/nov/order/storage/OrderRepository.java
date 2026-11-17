package com.nov.order.storage;

import com.nov.order.model.ProcessedOrder;
import com.nov.order.model.Product;
import com.nov.order.storage.internal.InMemoryOrderRepository;

import java.util.List;

public interface OrderRepository<T extends Product> {
    static <T extends Product> OrderRepository<T> inMemory() {
        return new InMemoryOrderRepository<>();
    }

    /**
     * Зберігає замовлення один раз. Повертає false, якщо його ідентифікатор уже є в сховищі.
     */
    boolean save(ProcessedOrder<T> order);

    List<ProcessedOrder<T>> findAll();
}
