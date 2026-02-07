package com.dipesh.service.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemPayloadTest
{
    @Test
    void exposesRecordComponents()
    {
        OrderItemPayload payload = new OrderItemPayload("p1", 2);

        assertEquals("p1", payload.productId());
        assertEquals(2, payload.quantity());
    }
}
