package com.dipesh.exception;

import com.dipesh.model.OrderState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidOrderStateTransitionExceptionTest
{
    @Test
    void buildsMessageWithStates()
    {
        InvalidOrderStateTransitionException exception = new InvalidOrderStateTransitionException(
                OrderState.PROCESSING,
                OrderState.CANCELLED
        );

        assertEquals("Invalid order state transition: PROCESSING -> CANCELLED", exception.getMessage());
    }
}
