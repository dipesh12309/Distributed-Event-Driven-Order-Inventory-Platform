package com.dipesh.service.events;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class OrderEventProducerTest
{
    @Test
    void sendsOrderCreatedEvent()
    {
        KafkaTemplate<String, Object> template = mock(KafkaTemplate.class);
        OrderEventProducer producer = new OrderEventProducer(template);
        OrderCreatedEvent event = new OrderCreatedEvent("order-1", "user-1", new BigDecimal("1.00"), List.of());

        producer.sendOrderCreatedEvent(event);

        verify(template).send("order-created-topic", "order-1", event);
    }
}
