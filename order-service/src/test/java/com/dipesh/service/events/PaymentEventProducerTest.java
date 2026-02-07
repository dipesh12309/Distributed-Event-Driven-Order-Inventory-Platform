package com.dipesh.service.events;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PaymentEventProducerTest
{
    @Test
    void sendsPaymentResultEvent()
    {
        KafkaTemplate<String, Object> template = mock(KafkaTemplate.class);
        PaymentEventProducer producer = new PaymentEventProducer(template);
        PaymentResultEvent event = new PaymentResultEvent("order-1", true);

        producer.sendPaymentResult(event);

        verify(template).send("payment-result-topic", "order-1", event);
    }
}
