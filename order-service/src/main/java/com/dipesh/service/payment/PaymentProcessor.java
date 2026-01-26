package com.dipesh.service.payment;

import com.dipesh.service.events.OrderCreatedEvent;
import com.dipesh.service.events.PaymentEventProducer;
import com.dipesh.service.events.PaymentResultEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProcessor
{

    private final PaymentGateway gateway;
    private final PaymentEventProducer producer;

    public void process(OrderCreatedEvent event)
    {

        boolean success = gateway.charge(event.userId(), event.amount().doubleValue());

        producer.sendPaymentResult(new PaymentResultEvent(event.orderId(), success));
    }
}
