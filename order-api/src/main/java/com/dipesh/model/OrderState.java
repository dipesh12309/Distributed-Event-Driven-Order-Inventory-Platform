package com.dipesh.model;

public enum OrderState {
    PROCESSING,        // order created, processing not finished
    CONFIRMED,  // order completed successfully
    CANCELLED   // order failed and closed
}

