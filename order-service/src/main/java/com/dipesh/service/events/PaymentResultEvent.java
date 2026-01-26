package com.dipesh.service.events;

public record PaymentResultEvent(
        String orderId,
        boolean success
)
{
}
