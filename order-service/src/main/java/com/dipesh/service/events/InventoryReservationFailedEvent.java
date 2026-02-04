package com.dipesh.service.events;

public record InventoryReservationFailedEvent(String orderId, String reason)
{
}
