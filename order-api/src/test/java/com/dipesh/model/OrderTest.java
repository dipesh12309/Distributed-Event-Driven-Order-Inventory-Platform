package com.dipesh.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class OrderTest
{
    @Test
    void newOrderSetsDefaults()
    {
        List<OrderItem> items = List.of(
                new OrderItem("p1", 1, new BigDecimal("10.00")),
                new OrderItem("p2", 2, new BigDecimal("5.50"))
        );

        Order order = new Order("user-1", items);

        assertNotNull(order.getOrderId());
        assertEquals("user-1", order.getUserId());
        assertEquals(OrderState.PROCESSING, order.getState());
        assertNotNull(order.getCreatedAt());
        assertEquals(items, order.getItems());
    }

    @Test
    void calculateAmountSumsItemPrices()
    {
        Order order = new Order("user-1", List.of(
                new OrderItem("p1", 1, new BigDecimal("10.00")),
                new OrderItem("p2", 2, new BigDecimal("5.50"))
        ));

        assertEquals(new BigDecimal("15.50"), order.calculateAmount());
    }

    @Test
    void constructorUsesProvidedValues()
    {
        Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
        List<OrderItem> items = List.of(new OrderItem("p1", 1, new BigDecimal("3.00")));

        Order order = new Order(createdAt, "order-1", "user-2", items, OrderState.CANCELLED);

        assertSame(createdAt, order.getCreatedAt());
        assertEquals("order-1", order.getOrderId());
        assertEquals("user-2", order.getUserId());
        assertEquals(items, order.getItems());
        assertEquals(OrderState.CANCELLED, order.getState());
    }
}
