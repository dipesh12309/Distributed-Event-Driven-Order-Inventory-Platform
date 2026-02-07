package com.dipesh.service.util;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.model.OrderState;
import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.entity.OrderItemEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderEntityMapperTest
{
    @Test
    void mapsDomainToEntity()
    {
        Order order = new Order("user-1", List.of(
                new OrderItem("p1", 1, new BigDecimal("3.00")),
                new OrderItem("p2", 2, new BigDecimal("4.00"))
        ));

        OrderEntity entity = OrderEntityMapper.toEntity(order);

        assertEquals(order.getOrderId(), entity.getOrderId());
        assertEquals(order.getUserId(), entity.getUserId());
        assertEquals(order.getState(), entity.getState());
        assertEquals(order.getCreatedAt(), entity.getCreatedAt());
        assertEquals(2, entity.getItems().size());
        OrderItemEntity firstItem = entity.getItems().get(0);
        assertEquals("p1", firstItem.getProductId());
        assertNotNull(firstItem.getOrder());
    }

    @Test
    void mapsEntityToDomain()
    {
        OrderEntity entity = new OrderEntity("order-1", "user-1", OrderState.CONFIRMED, Instant.parse("2024-02-01T00:00:00Z"));
        entity.addItem(new OrderItemEntity("p1", 1, new BigDecimal("2.50")));

        Order order = OrderEntityMapper.toDomain(entity);

        assertEquals(entity.getOrderId(), order.getOrderId());
        assertEquals(entity.getUserId(), order.getUserId());
        assertEquals(entity.getState(), order.getState());
        assertEquals(entity.getCreatedAt(), order.getCreatedAt());
        assertEquals(1, order.getItems().size());
        assertEquals("p1", order.getItems().get(0).getProductId());
    }
}
