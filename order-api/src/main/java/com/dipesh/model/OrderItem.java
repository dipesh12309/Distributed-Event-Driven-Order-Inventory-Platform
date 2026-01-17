package com.dipesh.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderItem {

    private final String productId;
    private final int quantity;
    private final BigDecimal price;

    public OrderItem(String productId, int quantity, BigDecimal price) {
        this.productId = Objects.requireNonNull(productId);
        this.quantity = quantity;
        this.price = Objects.requireNonNull(price);
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
