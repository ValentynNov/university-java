package com.nov.order.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Electronics(String name, BigDecimal price, String brand) implements Product {
}
