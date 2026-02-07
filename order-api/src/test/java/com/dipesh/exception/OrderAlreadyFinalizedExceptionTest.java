package com.dipesh.exception;

import com.dipesh.model.OrderState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderAlreadyFinalizedExceptionTest
{
    @Test
    void buildsMessageWithState()
    {
        OrderAlreadyFinalizedException exception = new OrderAlreadyFinalizedException(OrderState.CONFIRMED);

        assertEquals("Order is already finalized in state: CONFIRMED", exception.getMessage());
    }
}
