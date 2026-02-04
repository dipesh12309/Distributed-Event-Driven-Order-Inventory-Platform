package com.dipesh.service.events;

import com.dipesh.service.application.InventoryService;
import com.dipesh.service.storage.ProcessedEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryReleaseConsumer
{
    private static final String CONSUMER_ID = "inventory-release";

    private final InventoryService inventoryService;
    private final ProcessedEventStore processedEventStore;

    @KafkaListener(topics = "inventory-release-topic", groupId = "inventory-group")
    public void consume(InventoryReleaseEvent event, Acknowledgment acknowledgment)
    {
        if (processedEventStore.isDuplicate(CONSUMER_ID, event.orderId()))
        {
            acknowledgment.acknowledge();
            return;
        }

        inventoryService.release(event.orderId(), event.items());
        acknowledgment.acknowledge();
    }
}
