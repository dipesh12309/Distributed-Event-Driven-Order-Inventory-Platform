package com.dipesh.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderItem {

    private String productId;
    private int quantity;
    private BigDecimal price;

    public OrderItem(String productId, int quantity, BigDecimal price) {
        this.productId = Objects.requireNonNull(productId);
        this.quantity = quantity;
        this.price = Objects.requireNonNull(price);
    }

    public OrderItem(){

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
