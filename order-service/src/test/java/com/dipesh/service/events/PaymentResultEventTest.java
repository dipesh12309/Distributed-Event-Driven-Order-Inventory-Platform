package com.dipesh.service.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentResultEventTest
{
    @Test
    void exposesRecordComponents()
    {
        PaymentResultEvent event = new PaymentResultEvent("order-1", true);

        assertEquals("order-1", event.orderId());
        assertEquals(true, event.success());
    }
}
