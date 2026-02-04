package com.dipesh.service.events;

import com.dipesh.service.application.InventoryService;
import com.dipesh.service.exception.InventoryInsufficientException;
import com.dipesh.service.storage.ProcessedEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryReservationConsumer
{
    private static final String CONSUMER_ID = "inventory-reserve";

    private final InventoryService inventoryService;
    private final InventoryEventProducer inventoryEventProducer;
    private final ProcessedEventStore processedEventStore;

    @KafkaListener(topics = "order-created-topic", groupId = "inventory-group")
    public void consume(OrderCreatedEvent event, Acknowledgment acknowledgment)
    {
        if (processedEventStore.isDuplicate(CONSUMER_ID, event.orderId()))
        {
            acknowledgment.acknowledge();
            return;
        }

        try
        {
            inventoryService.reserve(event.orderId(), event.items());
            inventoryEventProducer.sendInventoryReserved(new InventoryReservedEvent(
                    event.orderId(),
                    event.userId(),
                    event.amount(),
                    event.items()
            ));
            acknowledgment.acknowledge();
        }
        catch (InventoryInsufficientException ex)
        {
            inventoryEventProducer.sendInventoryReservationFailed(new InventoryReservationFailedEvent(event.orderId(), ex.getMessage()));
            acknowledgment.acknowledge();
        }
    }
}
