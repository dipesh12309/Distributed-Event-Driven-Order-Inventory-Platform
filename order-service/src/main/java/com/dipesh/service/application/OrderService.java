package com.dipesh.service.application;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.service.repo.OrderRepository;

import java.util.List;
import java.util.Objects;

public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    /**
     * Use case: Create a new order
     */
    public Order createOrder(String userId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }

        Order order = new Order(userId, items);
        repository.save(order);
        return order;
    }

    public Order getOrder(String orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
