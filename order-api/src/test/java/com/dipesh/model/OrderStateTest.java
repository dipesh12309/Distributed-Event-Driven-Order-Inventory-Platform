package com.dipesh.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderStateTest
{
    @Test
    void exposesExpectedValues()
    {
        assertArrayEquals(new OrderState[]
                {
                        OrderState.PROCESSING,
                        OrderState.CONFIRMED,
                        OrderState.CANCELLED
                }, OrderState.values());
    }

    @Test
    void valueOfResolvesEnum()
    {
        assertEquals(OrderState.CONFIRMED, OrderState.valueOf("CONFIRMED"));
    }
}
