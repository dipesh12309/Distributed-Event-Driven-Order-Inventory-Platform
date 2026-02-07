package com.dipesh.service.events;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryReleaseEventTest
{
    @Test
    void exposesRecordComponents()
    {
        List<OrderItemPayload> items = List.of(new OrderItemPayload("p1", 1));
        InventoryReleaseEvent event = new InventoryReleaseEvent("order-1", items);

        assertEquals("order-1", event.orderId());
        assertEquals(items, event.items());
    }
}
