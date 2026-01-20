package com.dipesh.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItemEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(nullable = false)
    private String productId;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    protected OrderItemEntity()
    {
    }

    public OrderItemEntity(String productId, int quantity, BigDecimal price)
    {
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId()
    {
        return id;
    }

    public OrderEntity getOrder()
    {
        return order;
    }

    void setOrder(OrderEntity order)
    {
        this.order = order;
    }

    public String getProductId()
    {
        return productId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    // getters
}
