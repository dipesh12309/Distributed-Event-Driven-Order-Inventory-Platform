package com.dipesh.service.application;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.service.repo.OrderRepository;

import java.util.List;
import java.util.Objects;

public class OrderService {

    private final OrderRepository repository;
    private final InMemoryIdempotencyStore idempotencyStore;

    public OrderService(OrderRepository repository,
                        InMemoryIdempotencyStore idempotencyStore) {
        this.repository = Objects.requireNonNull(repository);
        this.idempotencyStore = Objects.requireNonNull(idempotencyStore);
    }

    /**
     * Idempotent create order
     */
    public Order createOrder(String idempotencyKey,
                             String userId,
                             List<OrderItem> items) {
        return idempotencyStore.get(idempotencyKey)
                .orElseGet(() -> createAndStore(idempotencyKey, userId, items));
    }

    private Order createAndStore(String key,
                                 String userId,
                                 List<OrderItem> items) {

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order order = new Order(userId, items);

        repository.save(order);
        idempotencyStore.put(key, order);

        return order;
    }

    public Order getOrder(String orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
