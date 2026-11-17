package com.nov.order.model;

import java.util.Objects;
import java.util.UUID;

public record Order<T extends Product>(UUID id, T product, int quantity) {
    public Order {
        Objects.requireNonNull(id, "Ідентифікатор замовлення обов’язковий.");
        Objects.requireNonNull(product, "Товар обов’язковий.");
    }
}
