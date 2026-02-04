package com.dipesh.service.application;

import com.dipesh.service.entity.InventoryEntity;
import com.dipesh.service.events.OrderItemPayload;
import com.dipesh.service.exception.InventoryInsufficientException;
import com.dipesh.service.repo.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService
{
    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository)
    {
        this.repository = repository;
    }

    @Transactional
    public void reserve(String orderId, List<OrderItemPayload> items)
    {
        for (OrderItemPayload item : items)
        {
            InventoryEntity entity = repository.findByProductIdForUpdate(item.productId())
                    .orElseThrow(() -> new InventoryInsufficientException("No inventory for product: " + item.productId()));

            if (entity.getAvailableQty() < item.quantity())
            {
                throw new InventoryInsufficientException("Insufficient stock for product: " + item.productId());
            }
            entity.setAvailableQty(entity.getAvailableQty() - item.quantity());
        }
    }

    @Transactional
    public void release(String orderId, List<OrderItemPayload> items)
    {
        for (OrderItemPayload item : items)
        {
            InventoryEntity entity = repository.findByProductIdForUpdate(item.productId())
                    .orElseThrow(() -> new InventoryInsufficientException("No inventory for product: " + item.productId()));
            entity.setAvailableQty(entity.getAvailableQty() + item.quantity());
        }
    }
}
