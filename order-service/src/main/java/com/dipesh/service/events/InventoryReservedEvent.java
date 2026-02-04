package com.dipesh.service.events;

import java.math.BigDecimal;
import java.util.List;

public record InventoryReservedEvent(String orderId, String userId, BigDecimal amount, List<OrderItemPayload> items)
{
}
