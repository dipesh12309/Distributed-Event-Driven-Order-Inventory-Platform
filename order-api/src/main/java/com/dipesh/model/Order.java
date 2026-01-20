package com.dipesh.model;

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
        this.state = OrderState.CREATED;
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

    public void markProcessing()
    {
        if (state != OrderState.CREATED)
        {
            throw new IllegalStateException("Order not in NEW state");
        }
        state = OrderState.PROCESSING;
    }

    public void markConfirmed()
    {
        if (state != OrderState.PROCESSING)
        {
            throw new IllegalStateException("Order not in PROCESSING state");
        }
        state = OrderState.CONFIRMED;
    }

    public void cancel()
    {
        if (state == OrderState.CONFIRMED)
        {
            throw new IllegalStateException("Confirmed order cannot be cancelled");
        }
        state = OrderState.CANCELLED;
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
}
