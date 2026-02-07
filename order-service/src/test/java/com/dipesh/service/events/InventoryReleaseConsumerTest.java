package com.dipesh.service.events;

import com.dipesh.service.application.InventoryService;
import com.dipesh.service.storage.ProcessedEventStore;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InventoryReleaseConsumerTest
{
    @Test
    void acknowledgesDuplicate()
    {
        InventoryService service = mock(InventoryService.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("inventory-release", "order-1")).thenReturn(true);

        InventoryReleaseConsumer consumer = new InventoryReleaseConsumer(service, store);

        consumer.consume(new InventoryReleaseEvent("order-1", List.of()), acknowledgment);

        verify(acknowledgment).acknowledge();
        verify(service, never()).release(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void releasesInventoryAndAcknowledges()
    {
        InventoryService service = mock(InventoryService.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("inventory-release", "order-1")).thenReturn(false);

        InventoryReleaseConsumer consumer = new InventoryReleaseConsumer(service, store);
        InventoryReleaseEvent event = new InventoryReleaseEvent("order-1", List.of(new OrderItemPayload("p1", 1)));

        consumer.consume(event, acknowledgment);

        verify(service).release("order-1", event.items());
        verify(acknowledgment).acknowledge();
    }
}
