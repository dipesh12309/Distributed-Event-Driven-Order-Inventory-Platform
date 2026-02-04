package com.dipesh.service.events;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent(String orderId, String userId, BigDecimal amount, List<OrderItemPayload> items)
{
}
