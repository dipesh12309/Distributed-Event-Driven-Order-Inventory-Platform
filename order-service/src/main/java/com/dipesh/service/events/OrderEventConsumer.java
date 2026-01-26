package com.dipesh.service.events;

import com.dipesh.service.payment.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer
{

    private final PaymentProcessor paymentProcessor;

    @KafkaListener(topics = "order-created-topic", groupId = "payment-group")
    public void consume(OrderCreatedEvent event)
    {
        paymentProcessor.process(event);
    }
}
