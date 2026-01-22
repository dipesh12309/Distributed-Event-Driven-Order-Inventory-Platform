package com.dipesh.service.exception;

public class OrderNotFoundException extends RuntimeException
{
    public OrderNotFoundException(String message){
        super(message);
    }
}
