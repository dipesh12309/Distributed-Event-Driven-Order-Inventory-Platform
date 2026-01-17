package com.dipesh.exception;

import com.dipesh.model.OrderState;

public class InvalidOrderStateTransitionException extends RuntimeException {

    public InvalidOrderStateTransitionException(
            OrderState from,
            OrderState to
    ) {
        super("Invalid order state transition: " + from + " -> " + to);
    }
}
