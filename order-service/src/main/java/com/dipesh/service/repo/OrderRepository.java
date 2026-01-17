package com.dipesh.service.repo;

import com.dipesh.model.Order;

import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(String orderId);
}
