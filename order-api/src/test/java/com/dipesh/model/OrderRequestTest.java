package com.dipesh.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderRequestTest
{
    @Test
    void defaultsToNullFields()
    {
        OrderRequest request = new OrderRequest();

        assertNull(request.getIdempotencyKey());
        assertNull(request.getUserId());
        assertNull(request.getItems());
    }

    @Test
    void settersAndGettersRoundTrip()
    {
        OrderRequest request = new OrderRequest();
        List<OrderItem> items = List.of(new OrderItem("p1", 1, new BigDecimal("9.99")));

        request.setIdempotencyKey("key-1");
        request.setUserId("user-1");
        request.setItems(items);

        assertEquals("key-1", request.getIdempotencyKey());
        assertEquals("user-1", request.getUserId());
        assertEquals(items, request.getItems());
    }
}
