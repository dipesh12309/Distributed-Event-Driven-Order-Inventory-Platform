package com.dipesh.service.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventProducer
{
    private static final Logger log = LoggerFactory.getLogger(InventoryEventProducer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, Object> kafkaTemplate)
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendInventoryReserved(InventoryReservedEvent event)
    {
        log.info("Publishing InventoryReservedEvent: {}", event);
        kafkaTemplate.send("inventory-reserved-topic", event.orderId(), event);
    }

    public void sendInventoryReservationFailed(InventoryReservationFailedEvent event)
    {
        log.info("Publishing InventoryReservationFailedEvent: {}", event);
        kafkaTemplate.send("inventory-reservation-failed-topic", event.orderId(), event);
    }

    public void sendInventoryRelease(InventoryReleaseEvent event)
    {
        log.info("Publishing InventoryReleaseEvent: {}", event);
        kafkaTemplate.send("inventory-release-topic", event.orderId(), event);
    }
}
