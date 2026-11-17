package com.nov.order.model;

import java.math.BigDecimal;

public record ProcessedOrder<T extends Product>(Order<T> order, BigDecimal total, String threadName) {
}
