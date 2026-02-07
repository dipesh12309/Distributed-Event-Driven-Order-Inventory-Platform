package com.dipesh.service.storage;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IdempotencyStoreTest
{
    @Test
    void isInterface()
    {
        assertTrue(IdempotencyStore.class.isInterface());
    }
}
