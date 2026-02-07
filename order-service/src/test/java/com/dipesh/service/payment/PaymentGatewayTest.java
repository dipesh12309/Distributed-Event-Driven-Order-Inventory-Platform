package com.dipesh.service.payment;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaymentGatewayTest
{
    @Test
    void chargeReturnsBoolean()
    {
        PaymentGateway gateway = new PaymentGateway();

        Boolean result = assertDoesNotThrow(() -> gateway.charge("user-1", 42.0));

        assertNotNull(result);
    }
}
