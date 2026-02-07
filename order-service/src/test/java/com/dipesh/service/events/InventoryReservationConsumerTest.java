package com.dipesh.service.events;

import com.dipesh.service.application.InventoryService;
import com.dipesh.service.exception.InventoryInsufficientException;
import com.dipesh.service.storage.ProcessedEventStore;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InventoryReservationConsumerTest
{
    @Test
    void acknowledgesDuplicate()
    {
        InventoryService service = mock(InventoryService.class);
        InventoryEventProducer producer = mock(InventoryEventProducer.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("inventory-reserve", "order-1")).thenReturn(true);

        InventoryReservationConsumer consumer = new InventoryReservationConsumer(service, producer, store);

        consumer.consume(new OrderCreatedEvent("order-1", "user-1", BigDecimal.ONE, List.of()), acknowledgment);

        verify(acknowledgment).acknowledge();
    }

    @Test
    void reservesInventoryAndPublishesReservedEvent()
    {
        InventoryService service = mock(InventoryService.class);
        InventoryEventProducer producer = mock(InventoryEventProducer.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("inventory-reserve", "order-1")).thenReturn(false);

        InventoryReservationConsumer consumer = new InventoryReservationConsumer(service, producer, store);
        List<OrderItemPayload> items = List.of(new OrderItemPayload("p1", 1));
        OrderCreatedEvent event = new OrderCreatedEvent("order-1", "user-1", BigDecimal.ONE, items);

        consumer.consume(event, acknowledgment);

        ArgumentCaptor<InventoryReservedEvent> captor = ArgumentCaptor.forClass(InventoryReservedEvent.class);
        verify(producer).sendInventoryReserved(captor.capture());
        InventoryReservedEvent reservedEvent = captor.getValue();
        assertEquals("order-1", reservedEvent.orderId());
        assertEquals(items, reservedEvent.items());
        verify(acknowledgment).acknowledge();
    }

    @Test
    void publishesFailedEventOnInsufficientInventory()
    {
        InventoryService service = mock(InventoryService.class);
        InventoryEventProducer producer = mock(InventoryEventProducer.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("inventory-reserve", "order-1")).thenReturn(false);

        doThrow(new InventoryInsufficientException("insufficient")).when(service)
                .reserve("order-1", List.of(new OrderItemPayload("p1", 1)));

        InventoryReservationConsumer consumer = new InventoryReservationConsumer(service, producer, store);
        OrderCreatedEvent event = new OrderCreatedEvent("order-1", "user-1", BigDecimal.ONE, List.of(new OrderItemPayload("p1", 1)));

        consumer.consume(event, acknowledgment);

        ArgumentCaptor<InventoryReservationFailedEvent> captor = ArgumentCaptor.forClass(InventoryReservationFailedEvent.class);
        verify(producer).sendInventoryReservationFailed(captor.capture());
        InventoryReservationFailedEvent failedEvent = captor.getValue();
        assertEquals("order-1", failedEvent.orderId());
        assertEquals("insufficient", failedEvent.reason());
        verify(acknowledgment).acknowledge();
    }
}
