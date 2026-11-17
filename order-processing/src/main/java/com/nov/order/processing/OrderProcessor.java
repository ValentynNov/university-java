package com.nov.order.processing;

import com.nov.order.model.Order;
import com.nov.order.model.ProcessedOrder;
import com.nov.order.model.Product;
import com.nov.order.storage.OrderRepository;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderProcessor<T extends Product> {
    private final Order<T> order;
    private final OrderRepository<T> repository;

    public OrderProcessor(Order<T> order, OrderRepository<T> repository) {
        this.order = Objects.requireNonNull(order);
        this.repository = Objects.requireNonNull(repository);
    }

    public ProcessedOrder<T> process() throws OrderProcessingException {
        if (order.quantity() <= 0) {
            throw new OrderProcessingException("Кількість товару має бути більшою за нуль.");
        }

        T product = order.product();
        if (product.name() == null || product.name().isBlank()) {
            throw new OrderProcessingException("Назва товару обов’язкова.");
        }

        BigDecimal price = product.price();
        if (price == null || price.signum() < 0) {
            throw new OrderProcessingException("Ціна товару має бути невід’ємною.");
        }

        BigDecimal total = price.multiply(BigDecimal.valueOf(order.quantity()));
        var result = new ProcessedOrder<>(order, total, Thread.currentThread().getName());
        if (!repository.save(result)) {
            throw new OrderProcessingException("Замовлення з таким ідентифікатором уже оброблено.");
        }
        return result;
    }
}
