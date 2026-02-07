package com.dipesh.service.events;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderCreatedEventTest
{
    @Test
    void exposesRecordComponents()
    {
        List<OrderItemPayload> items = List.of(new OrderItemPayload("p1", 2));
        OrderCreatedEvent event = new OrderCreatedEvent("order-1", "user-1", new BigDecimal("9.99"), items);

        assertEquals("order-1", event.orderId());
        assertEquals("user-1", event.userId());
        assertEquals(new BigDecimal("9.99"), event.amount());
        assertEquals(items, event.items());
    }
}
