package com.dipesh.service.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderNotFoundExceptionTest
{
    @Test
    void exposesMessage()
    {
        OrderNotFoundException exception = new OrderNotFoundException("missing");

        assertEquals("missing", exception.getMessage());
    }
}
