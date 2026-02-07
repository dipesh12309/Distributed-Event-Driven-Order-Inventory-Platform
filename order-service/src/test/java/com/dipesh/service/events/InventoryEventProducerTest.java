package com.dipesh.service.events;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class InventoryEventProducerTest
{
    @Test
    void sendsInventoryReservedEvent()
    {
        KafkaTemplate<String, Object> template = mock(KafkaTemplate.class);
        InventoryEventProducer producer = new InventoryEventProducer(template);
        InventoryReservedEvent event = new InventoryReservedEvent("order-1", "user-1", java.math.BigDecimal.ONE, List.of());

        producer.sendInventoryReserved(event);

        verify(template).send("inventory-reserved-topic", "order-1", event);
    }

    @Test
    void sendsInventoryReservationFailedEvent()
    {
        KafkaTemplate<String, Object> template = mock(KafkaTemplate.class);
        InventoryEventProducer producer = new InventoryEventProducer(template);
        InventoryReservationFailedEvent event = new InventoryReservationFailedEvent("order-1", "reason");

        producer.sendInventoryReservationFailed(event);

        verify(template).send("inventory-reservation-failed-topic", "order-1", event);
    }

    @Test
    void sendsInventoryReleaseEvent()
    {
        KafkaTemplate<String, Object> template = mock(KafkaTemplate.class);
        InventoryEventProducer producer = new InventoryEventProducer(template);
        InventoryReleaseEvent event = new InventoryReleaseEvent("order-1", List.of());

        producer.sendInventoryRelease(event);

        verify(template).send("inventory-release-topic", "order-1", event);
    }
}
