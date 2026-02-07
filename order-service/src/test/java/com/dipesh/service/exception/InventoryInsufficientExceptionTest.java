package com.dipesh.service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryInsufficientExceptionTest
{
    @Test
    void exposesMessage()
    {
        InventoryInsufficientException exception = new InventoryInsufficientException("low");

        assertEquals("low", exception.getMessage());
    }
}
