package com.dipesh.service.events;

import com.dipesh.service.payment.PaymentProcessor;
import com.dipesh.service.storage.ProcessedEventStore;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.support.Acknowledgment;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderEventConsumerTest
{
    @Test
    void acknowledgesDuplicate()
    {
        PaymentProcessor processor = mock(PaymentProcessor.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("payment-processor", "order-1")).thenReturn(true);

        OrderEventConsumer consumer = new OrderEventConsumer(processor, store);

        consumer.consume(new InventoryReservedEvent("order-1", "user-1", BigDecimal.ONE, List.of()), acknowledgment);

        verify(acknowledgment).acknowledge();
    }

    @Test
    void forwardsPaymentProcessing()
    {
        PaymentProcessor processor = mock(PaymentProcessor.class);
        ProcessedEventStore store = mock(ProcessedEventStore.class);
        Acknowledgment acknowledgment = mock(Acknowledgment.class);
        when(store.isDuplicate("payment-processor", "order-1")).thenReturn(false);

        OrderEventConsumer consumer = new OrderEventConsumer(processor, store);
        InventoryReservedEvent event = new InventoryReservedEvent("order-1", "user-1", BigDecimal.ONE, List.of());

        consumer.consume(event, acknowledgment);

        ArgumentCaptor<OrderCreatedEvent> captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(processor).process(captor.capture());
        OrderCreatedEvent createdEvent = captor.getValue();
        assertEquals("order-1", createdEvent.orderId());
        assertEquals("user-1", createdEvent.userId());
        assertEquals(BigDecimal.ONE, createdEvent.amount());
        verify(acknowledgment).acknowledge();
    }
}
