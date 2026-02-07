package com.dipesh.service.payment;

import com.dipesh.service.events.OrderCreatedEvent;
import com.dipesh.service.events.PaymentEventProducer;
import com.dipesh.service.events.PaymentResultEvent;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentProcessorTest
{
    @Test
    void processesPaymentAndPublishesResult()
    {
        PaymentGateway gateway = mock(PaymentGateway.class);
        PaymentEventProducer producer = mock(PaymentEventProducer.class);
        PaymentProcessor processor = new PaymentProcessor(gateway, producer);

        when(gateway.charge("user-1", 12.5)).thenReturn(true);

        processor.process(new OrderCreatedEvent("order-1", "user-1", new BigDecimal("12.50"), java.util.List.of()));

        verify(producer).sendPaymentResult(new PaymentResultEvent("order-1", true));
    }

    @Test
    void publishesFailureResultWhenGatewayDeclines()
    {
        PaymentGateway gateway = mock(PaymentGateway.class);
        PaymentEventProducer producer = mock(PaymentEventProducer.class);
        PaymentProcessor processor = new PaymentProcessor(gateway, producer);

        when(gateway.charge("user-2", 8.0)).thenReturn(false);

        processor.process(new OrderCreatedEvent("order-2", "user-2", new BigDecimal("8.00"), java.util.List.of()));

        verify(producer).sendPaymentResult(new PaymentResultEvent("order-2", false));
    }
}
