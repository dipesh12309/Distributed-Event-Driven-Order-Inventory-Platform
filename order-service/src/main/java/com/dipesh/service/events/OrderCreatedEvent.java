package com.dipesh.service.events;

import java.math.BigDecimal;

public record OrderCreatedEvent(String orderId, String userId, BigDecimal amount)
{
}
