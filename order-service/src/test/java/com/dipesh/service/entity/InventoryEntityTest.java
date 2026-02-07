package com.dipesh.service.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryEntityTest
{
    @Test
    void constructorSetsFields()
    {
        InventoryEntity entity = new InventoryEntity("p1", 5);

        assertEquals("p1", entity.getProductId());
        assertEquals(5, entity.getAvailableQty());
    }

    @Test
    void settersUpdateFields()
    {
        InventoryEntity entity = new InventoryEntity("p2", 1);

        entity.setAvailableQty(3);

        assertEquals(3, entity.getAvailableQty());
    }
}
