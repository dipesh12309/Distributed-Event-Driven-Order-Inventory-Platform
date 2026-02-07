package com.dipesh.service.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryReservationFailedEventTest
{
    @Test
    void exposesRecordComponents()
    {
        InventoryReservationFailedEvent event = new InventoryReservationFailedEvent("order-1", "reason");

        assertEquals("order-1", event.orderId());
        assertEquals("reason", event.reason());
    }
}
