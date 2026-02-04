package com.dipesh.service.events;

import java.util.List;

public record InventoryReleaseEvent(String orderId, List<OrderItemPayload> items)
{
}
