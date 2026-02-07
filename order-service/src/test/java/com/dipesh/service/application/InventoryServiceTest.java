package com.dipesh.service.application;

import com.dipesh.service.entity.InventoryEntity;
import com.dipesh.service.events.OrderItemPayload;
import com.dipesh.service.exception.InventoryInsufficientException;
import com.dipesh.service.repo.InventoryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryServiceTest
{
    @Test
    void reserveReducesAvailableQuantity()
    {
        InventoryRepository repository = mock(InventoryRepository.class);
        InventoryEntity entity = new InventoryEntity("p1", 5);
        when(repository.findByProductIdForUpdate("p1")).thenReturn(Optional.of(entity));

        InventoryService service = new InventoryService(repository);

        service.reserve("order-1", List.of(new OrderItemPayload("p1", 2)));

        assertEquals(3, entity.getAvailableQty());
    }

    @Test
    void reserveThrowsWhenInsufficient()
    {
        InventoryRepository repository = mock(InventoryRepository.class);
        InventoryEntity entity = new InventoryEntity("p1", 1);
        when(repository.findByProductIdForUpdate("p1")).thenReturn(Optional.of(entity));

        InventoryService service = new InventoryService(repository);

        assertThrows(InventoryInsufficientException.class,
                () -> service.reserve("order-1", List.of(new OrderItemPayload("p1", 2))));
    }

    @Test
    void releaseIncreasesAvailableQuantity()
    {
        InventoryRepository repository = mock(InventoryRepository.class);
        InventoryEntity entity = new InventoryEntity("p1", 1);
        when(repository.findByProductIdForUpdate("p1")).thenReturn(Optional.of(entity));

        InventoryService service = new InventoryService(repository);

        service.release("order-1", List.of(new OrderItemPayload("p1", 2)));

        assertEquals(3, entity.getAvailableQty());
    }
}
