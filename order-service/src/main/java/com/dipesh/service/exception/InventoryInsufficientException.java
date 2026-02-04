package com.dipesh.service.exception;

public class InventoryInsufficientException extends RuntimeException
{
    public InventoryInsufficientException(String message)
    {
        super(message);
    }
}
