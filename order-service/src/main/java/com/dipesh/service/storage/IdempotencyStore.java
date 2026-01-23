package com.dipesh.service.storage;

import com.dipesh.model.Order;

import java.util.Optional;

public interface IdempotencyStore
{
    public Optional<String> get(String key);

    public void put(String key, String order);
}
