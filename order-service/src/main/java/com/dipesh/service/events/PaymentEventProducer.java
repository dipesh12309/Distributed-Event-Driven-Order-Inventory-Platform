package com.dipesh.service.events;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer
{

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentResult(PaymentResultEvent event)
    {
        kafkaTemplate.send("payment-result-topic", event.orderId(), event);
    }
}
