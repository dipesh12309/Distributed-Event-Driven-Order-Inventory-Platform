package com.dipesh.service.events;

import com.dipesh.model.OrderState;
import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.entity.OrderItemEntity;
import com.dipesh.service.repo.OrderRepository;
import com.dipesh.service.storage.ProcessedEventStore;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PaymentResultConsumerTest
{
    @Test
    void acknowledgesDuplicate()
    {
        OrderRepository repository = mock(OrderRepository.class);
        InventoryEventProducer producer = mock(InventoryEventProducer.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("payment-result", "order-1")).thenReturn(true);

        PaymentResultConsumer consumer = new PaymentResultConsumer(repository, producer, store);

        consumer.consume(new PaymentResultEvent("order-1", true), acknowledgment);

        verify(acknowledgment).acknowledge();
    }

    @Test
    void confirmsOrderOnSuccess()
    {
        OrderRepository repository = mock(OrderRepository.class);
        InventoryEventProducer producer = mock(InventoryEventProducer.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("payment-result", "order-1")).thenReturn(false);

        OrderEntity entity = new OrderEntity("order-1", "user-1", OrderState.PROCESSING, Instant.now());
        when(repository.findById("order-1")).thenReturn(Optional.of(entity));

        PaymentResultConsumer consumer = new PaymentResultConsumer(repository, producer, store);

        consumer.consume(new PaymentResultEvent("order-1", true), acknowledgment);

        assertEquals(OrderState.CONFIRMED, entity.getState());
        verify(acknowledgment).acknowledge();
    }

    @Test
    void cancelsOrderAndReleasesInventoryOnFailure()
    {
        OrderRepository repository = mock(OrderRepository.class);
        InventoryEventProducer producer = mock(InventoryEventProducer.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("payment-result", "order-1")).thenReturn(false);

        OrderEntity entity = new OrderEntity("order-1", "user-1", OrderState.PROCESSING, Instant.now());
        entity.addItem(new OrderItemEntity("p1", 2, new BigDecimal("3.00")));
        when(repository.findById("order-1")).thenReturn(Optional.of(entity));

        PaymentResultConsumer consumer = new PaymentResultConsumer(repository, producer, store);

        consumer.consume(new PaymentResultEvent("order-1", false), acknowledgment);

        assertEquals(OrderState.CANCELLED, entity.getState());
        ArgumentCaptor<InventoryReleaseEvent> captor = ArgumentCaptor.forClass(InventoryReleaseEvent.class);
        verify(producer).sendInventoryRelease(captor.capture());
        InventoryReleaseEvent event = captor.getValue();
        assertEquals("order-1", event.orderId());
        assertEquals(1, event.items().size());
        assertEquals("p1", event.items().get(0).productId());
        verify(acknowledgment).acknowledge();
    }
}
