package com.dipesh.service.application;

import com.dipesh.model.Order;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryIdempotencyStore {

    private final ConcurrentMap<String, Order> store = new ConcurrentHashMap<>();

    public Optional<Order> get(String key) {
        return Optional.ofNullable(store.get(key));
    }

    public void put(String key, Order order) {
        store.putIfAbsent(key, order);
    }
}
