package com.dipesh.service.application;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.service.paymentservice.Payment;
import com.dipesh.service.paymentservice.PaymentService;
import com.dipesh.service.paymentservice.PaymentStatus;
import com.dipesh.service.repo.OrderRepository;

import java.util.List;
import java.util.Objects;

public class OrderService {

    private final OrderRepository repository;
    private final InMemoryIdempotencyStore idempotencyStore;
    private final PaymentService paymentService;

    public OrderService(OrderRepository repository,
                        InMemoryIdempotencyStore idempotencyStore, PaymentService paymentService) {
        this.repository = Objects.requireNonNull(repository);
        this.idempotencyStore = Objects.requireNonNull(idempotencyStore);
        this.paymentService = paymentService;
    }

    public Order placeOrder(String key, String userId, List<OrderItem> items) {

        // 1. If this key was already processed, return the same order
        Order existing = idempotencyStore.get(key).orElse(null);
        if (existing != null) {
            return existing;
        }

        // 2. Otherwise process normally
        Order order = new Order(userId, items);
        repository.save(order);

        order.markProcessing();

        Payment payment = paymentService.initiatePayment(order);
        Payment finalPayment = paymentService.confirmPayment(payment.getPaymentId());

        if (finalPayment.getStatus() == PaymentStatus.FAILED) {
            order.cancel();
            idempotencyStore.put(key, order);
            return order;
        }

        order.markConfirmed();
        idempotencyStore.put(key, order);
        return order;
    }



    public Order getOrder(String orderId) {
        return repository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }
}
