package com.dipesh.service.entity;

import com.dipesh.model.OrderState;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderEntityTest
{
    @Test
    void addItemAssociatesOrder()
    {
        OrderEntity order = new OrderEntity("order-1", "user-1", OrderState.PROCESSING, Instant.parse("2024-01-01T00:00:00Z"));
        OrderItemEntity item = new OrderItemEntity("p1", 1, new BigDecimal("4.00"));

        order.addItem(item);

        assertEquals(1, order.getItems().size());
        assertEquals(order, item.getOrder());
    }

    @Test
    void markConfirmedTransitionsFromProcessing()
    {
        OrderEntity order = new OrderEntity("order-1", "user-1", OrderState.PROCESSING, Instant.now());

        order.markConfirmed();

        assertEquals(OrderState.CONFIRMED, order.getState());
    }

    @Test
    void markConfirmedFailsIfNotProcessing()
    {
        OrderEntity order = new OrderEntity("order-1", "user-1", OrderState.CANCELLED, Instant.now());

        assertThrows(IllegalStateException.class, order::markConfirmed);
    }

    @Test
    void markCancelledFromProcessing()
    {
        OrderEntity order = new OrderEntity("order-1", "user-1", OrderState.PROCESSING, Instant.now());

        order.markCancelled();

        assertEquals(OrderState.CANCELLED, order.getState());
    }

    @Test
    void markCancelledFailsIfConfirmed()
    {
        OrderEntity order = new OrderEntity("order-1", "user-1", OrderState.CONFIRMED, Instant.now());

        assertThrows(IllegalStateException.class, order::markCancelled);
    }
}
