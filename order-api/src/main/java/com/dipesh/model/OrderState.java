package com.dipesh.model;

public enum OrderState {
    NEW,        // order created, processing not finished
    PROCESSING, // system is working on it
    CONFIRMED,  // order completed successfully
    CANCELLED   // order failed and closed
}

