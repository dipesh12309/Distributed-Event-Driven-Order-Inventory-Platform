package com.dipesh.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order
{

    private final String orderId;
    private final String userId;
    private final List<OrderItem> items;
    private final Instant createdAt;

    private OrderState state;

    public Order(String userId, List<OrderItem> items)
    {
        this.orderId = UUID.randomUUID().toString();
        this.userId = Objects.requireNonNull(userId);
        this.items = List.copyOf(items);
        this.state = OrderState.PROCESSING;
        this.createdAt = Instant.now();
    }

    public Order(Instant createdAt, String orderId, String userId, List<OrderItem> items, OrderState state)
    {
        this.createdAt = createdAt;
        this.orderId = orderId;
        this.userId = userId;
        this.items = items;
        this.state = state;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public String getUserId()
    {
        return userId;
    }

    public List<OrderItem> getItems()
    {
        return items;
    }

    public OrderState getState()
    {
        return state;
    }

    public Instant getCreatedAt()
    {
        return createdAt;
    }

    public BigDecimal calculateAmount()
    {
        return items.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
