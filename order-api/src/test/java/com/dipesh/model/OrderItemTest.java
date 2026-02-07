package com.dipesh.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderItemTest
{
    @Test
    void constructorSetsFields()
    {
        OrderItem item = new OrderItem("product-1", 3, new BigDecimal("12.50"));

        assertEquals("product-1", item.getProductId());
        assertEquals(3, item.getQuantity());
        assertEquals(new BigDecimal("12.50"), item.getPrice());
    }

    @Test
    void constructorRequiresProductId()
    {
        assertThrows(NullPointerException.class, () -> new OrderItem(null, 1, BigDecimal.ONE));
    }

    @Test
    void constructorRequiresPrice()
    {
        assertThrows(NullPointerException.class, () -> new OrderItem("product", 1, null));
    }
}
