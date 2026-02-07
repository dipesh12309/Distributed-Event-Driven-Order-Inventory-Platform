package com.dipesh.service.handler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest
{
    @Test
    void exposesRecordComponents()
    {
        ErrorResponse response = new ErrorResponse("CODE", "message");

        assertEquals("CODE", response.code());
        assertEquals("message", response.message());
    }
}
