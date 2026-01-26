package com.dipesh.service.entity;

import com.dipesh.model.OrderState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity
{

    @Id
    @Column(name = "order_id", length = 36)
    private String orderId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderState state;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Version
    private int version;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();

    protected OrderEntity()
    {
    }

    public OrderEntity(String orderId, String userId, OrderState state, Instant createdAt)
    {
        this.orderId = orderId;
        this.userId = userId;
        this.state = state;
        this.createdAt = createdAt;
    }

    public void addItem(OrderItemEntity item)
    {
        items.add(item);
        item.setOrder(this);
    }

    public void markConfirmed()
    {
        if (state != OrderState.PROCESSING)
        {
            throw new IllegalStateException("Order not in PROCESSING state");
        }
        state = OrderState.CONFIRMED;
    }

    public void markCancelled()
    {
        if (state == OrderState.CONFIRMED)
        {
            throw new IllegalStateException("Confirmed order cannot be cancelled");
        }
        state = OrderState.CANCELLED;
    }

}

