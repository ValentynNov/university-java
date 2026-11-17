package com.nov.order.model;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Clothing(String name, BigDecimal price, String size) implements Product {
}
