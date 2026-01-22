package com.dipesh.service.controller;

import com.dipesh.model.Order;
import com.dipesh.model.OrderRequest;
import com.dipesh.service.application.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController
{

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService)
    {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order createOrder(@RequestBody OrderRequest request)
    {
        log.debug("Order request received {} ", request);
        return orderService.placeOrder(request.getIdempotencyKey(), request.getUserId(), request.getItems());
    }

    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId)
    {
        log.debug("Order id : {} ", orderId);
        return orderService.getOrder(orderId);
    }

}
