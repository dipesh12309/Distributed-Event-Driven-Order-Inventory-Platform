package com.dipesh.service.application;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.exception.OrderNotFoundException;
import com.dipesh.service.paymentservice.Payment;
import com.dipesh.service.paymentservice.PaymentService;
import com.dipesh.service.paymentservice.PaymentStatus;
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
    private final PaymentService paymentService;

    public OrderService(OrderRepository repository, IdempotencyStore idempotencyStore, PaymentService paymentService)
    {
        this.repository = Objects.requireNonNull(repository);
        this.idempotencyStore = Objects.requireNonNull(idempotencyStore);
        this.paymentService = paymentService;
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

        order.markProcessing();

        Payment payment = paymentService.initiatePayment(order);
        Payment finalPayment = paymentService.confirmPayment(payment.getPaymentId());

        if (finalPayment.getStatus() == PaymentStatus.FAILED)
        {
            order.cancel();
            idempotencyStore.put(key, order.getOrderId());
            return order;
        }

        order.markConfirmed();
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
