package com.dipesh.exception;

import com.dipesh.model.OrderState;

public class OrderAlreadyFinalizedException extends RuntimeException {

    public OrderAlreadyFinalizedException(OrderState state) {
        super("Order is already finalized in state: " + state);
    }
}
