package com.dipesh.service.events;

import com.dipesh.model.OrderState;
import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.repo.OrderRepository;
import com.dipesh.service.storage.ProcessedEventStore;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.support.Acknowledgment;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InventoryReservationFailedConsumerTest
{
    @Test
    void acknowledgesDuplicate()
    {
        OrderRepository repository = mock(OrderRepository.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("inventory-failed-order", "order-1")).thenReturn(true);

        InventoryReservationFailedConsumer consumer = new InventoryReservationFailedConsumer(repository, store);

        consumer.consume(new InventoryReservationFailedEvent("order-1", "reason"), acknowledgment);

        verify(acknowledgment).acknowledge();
    }

    @Test
    void marksOrderCancelled()
    {
        OrderRepository repository = mock(OrderRepository.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("inventory-failed-order", "order-1")).thenReturn(false);

        OrderEntity entity = new OrderEntity("order-1", "user-1", OrderState.PROCESSING, Instant.now());
        when(repository.findById("order-1")).thenReturn(Optional.of(entity));

        InventoryReservationFailedConsumer consumer = new InventoryReservationFailedConsumer(repository, store);

        consumer.consume(new InventoryReservationFailedEvent("order-1", "reason"), acknowledgment);

        assertEquals(OrderState.CANCELLED, entity.getState());
        verify(acknowledgment).acknowledge();
    }
}
