package com.dipesh.service.entity;

import com.dipesh.model.OrderState;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderItemEntityTest
{
    @Test
    void constructorSetsFields()
    {
        OrderItemEntity entity = new OrderItemEntity("p1", 2, new BigDecimal("3.50"));

        assertEquals("p1", entity.getProductId());
        assertEquals(2, entity.getQuantity());
        assertEquals(new BigDecimal("3.50"), entity.getPrice());
        assertNull(entity.getOrder());
    }

    @Test
    void orderIsAssignedWhenAddedToOrderEntity()
    {
        OrderEntity order = new OrderEntity("order-1", "user-1", OrderState.PROCESSING, Instant.parse("2024-01-01T00:00:00Z"));
        OrderItemEntity entity = new OrderItemEntity("p1", 1, new BigDecimal("2.00"));

        order.addItem(entity);

        assertEquals(order, entity.getOrder());
    }
}
