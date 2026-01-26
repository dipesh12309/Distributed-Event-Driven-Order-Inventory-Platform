package com.dipesh.service.application;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.events.OrderCreatedEvent;
import com.dipesh.service.events.OrderEventProducer;
import com.dipesh.service.exception.OrderNotFoundException;
import com.dipesh.service.repo.OrderRepository;
import com.dipesh.service.storage.IdempotencyStore;
import com.dipesh.service.util.OrderEntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderService
{

    private final OrderRepository repository;
    private final IdempotencyStore idempotencyStore;
    private final OrderEventProducer orderEventProducer;

    public OrderService(OrderRepository repository, IdempotencyStore idempotencyStore, OrderEventProducer orderEventProducer)
    {
        this.repository = Objects.requireNonNull(repository);
        this.idempotencyStore = Objects.requireNonNull(idempotencyStore);
        this.orderEventProducer = orderEventProducer;
    }

    public Order placeOrder(String key, String userId, List<OrderItem> items)
    {
        String existing = idempotencyStore.get(key).orElse(null);
        if (existing != null)
        {
            OrderEntity referenceById = repository.getReferenceById(existing);
            return OrderEntityMapper.toDomain(referenceById);
        }

        Order order = new Order(userId, items);
        OrderEntity entity = OrderEntityMapper.toEntity(order);
        repository.save(entity);

        orderEventProducer.sendOrderCreatedEvent(new OrderCreatedEvent(order.getOrderId(), userId, order.calculateAmount()));

        idempotencyStore.put(key, order.getOrderId());
        return order;
    }

    public Order getOrder(String orderId)
    {
        UUID id = UUID.fromString(orderId);
        OrderEntity orderEntity = repository.findById(String.valueOf(id))
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));
        return OrderEntityMapper.toDomain(orderEntity);
    }
}
