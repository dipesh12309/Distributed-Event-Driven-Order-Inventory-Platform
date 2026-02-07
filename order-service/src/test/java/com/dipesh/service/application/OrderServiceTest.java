package com.dipesh.service.application;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.model.OrderState;
import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.events.OrderCreatedEvent;
import com.dipesh.service.events.OrderEventProducer;
import com.dipesh.service.exception.OrderNotFoundException;
import com.dipesh.service.repo.OrderRepository;
import com.dipesh.service.storage.IdempotencyStore;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServiceTest
{
    @Test
    void returnsExistingOrderWhenIdempotentKeyPresent()
    {
        OrderRepository repository = mock(OrderRepository.class);
        IdempotencyStore store = mock(IdempotencyStore.class);
        OrderEventProducer producer = mock(OrderEventProducer.class);

        OrderEntity entity = new OrderEntity("order-1", "user-1", OrderState.CONFIRMED, Instant.now());
        when(store.get("key-1")).thenReturn(Optional.of("order-1"));
        when(repository.getReferenceById("order-1")).thenReturn(entity);

        OrderService service = new OrderService(repository, store, producer);

        Order order = service.placeOrder("key-1", "user-1", List.of());

        assertEquals("order-1", order.getOrderId());
        assertEquals("user-1", order.getUserId());
        assertEquals(OrderState.CONFIRMED, order.getState());
    }

    @Test
    void createsNewOrderAndPublishesEvent()
    {
        OrderRepository repository = mock(OrderRepository.class);
        IdempotencyStore store = mock(IdempotencyStore.class);
        OrderEventProducer producer = mock(OrderEventProducer.class);
        when(store.get("key-2")).thenReturn(Optional.empty());

        OrderService service = new OrderService(repository, store, producer);
        List<OrderItem> items = List.of(new OrderItem("p1", 1, new BigDecimal("2.50")));

        Order order = service.placeOrder("key-2", "user-2", items);

        ArgumentCaptor<OrderCreatedEvent> captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(producer).sendOrderCreatedEvent(captor.capture());
        OrderCreatedEvent event = captor.getValue();

        assertEquals(order.getOrderId(), event.orderId());
        assertEquals("user-2", event.userId());
        assertEquals(new BigDecimal("2.50"), event.amount());
        assertEquals(1, event.items().size());
        verify(store).put("key-2", order.getOrderId());
        verify(repository).save(org.mockito.ArgumentMatchers.any(OrderEntity.class));
    }

    @Test
    void getOrderThrowsWhenNotFound()
    {
        OrderRepository repository = mock(OrderRepository.class);
        IdempotencyStore store = mock(IdempotencyStore.class);
        OrderEventProducer producer = mock(OrderEventProducer.class);

        UUID id = UUID.randomUUID();
        when(repository.findById(id.toString())).thenReturn(Optional.empty());

        OrderService service = new OrderService(repository, store, producer);

        assertThrows(OrderNotFoundException.class, () -> service.getOrder(id.toString()));
    }

    @Test
    void getOrderReturnsMappedDomain()
    {
        OrderRepository repository = mock(OrderRepository.class);
        IdempotencyStore store = mock(IdempotencyStore.class);
        OrderEventProducer producer = mock(OrderEventProducer.class);

        UUID id = UUID.randomUUID();
        OrderEntity entity = new OrderEntity(id.toString(), "user-3", OrderState.CANCELLED, Instant.now());
        when(repository.findById(id.toString())).thenReturn(Optional.of(entity));

        OrderService service = new OrderService(repository, store, producer);

        Order order = service.getOrder(id.toString());

        assertEquals(id.toString(), order.getOrderId());
        assertEquals("user-3", order.getUserId());
        assertEquals(OrderState.CANCELLED, order.getState());
    }
}
