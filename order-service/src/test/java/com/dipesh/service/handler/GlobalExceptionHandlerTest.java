package com.dipesh.service.handler;

import com.dipesh.service.exception.OrderNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest
{
    @Test
    void handlesOrderNotFound()
    {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ErrorResponse response = handler.handleNotFound(new OrderNotFoundException("missing"));

        assertEquals("ORDER_NOT_FOUND", response.code());
        assertEquals("missing", response.message());
    }

    @Test
    void handlesBadRequest()
    {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ErrorResponse response = handler.handleBadRequest(new IllegalArgumentException("bad"));

        assertEquals("BAD_REQUEST", response.code());
        assertEquals("bad", response.message());
    }
}
