package com.dipesh.service.controller;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.model.OrderRequest;
import com.dipesh.service.application.OrderService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderControllerTest
{
    @Test
    void createOrderDelegatesToService()
    {
        OrderService orderService = mock(OrderService.class);
        OrderController controller = new OrderController(orderService);
        OrderRequest request = new OrderRequest();
        request.setIdempotencyKey("key-1");
        request.setUserId("user-1");
        List<OrderItem> items = List.of(new OrderItem("p1", 1, new BigDecimal("2.50")));
        request.setItems(items);

        Order order = new Order("user-1", items);
        when(orderService.placeOrder("key-1", "user-1", items)).thenReturn(order);

        Order result = controller.createOrder(request);

        assertSame(order, result);
        verify(orderService).placeOrder("key-1", "user-1", items);
    }

    @Test
    void getOrderDelegatesToService()
    {
        OrderService orderService = mock(OrderService.class);
        OrderController controller = new OrderController(orderService);
        Order order = new Order("user-1", List.of(new OrderItem("p1", 1, new BigDecimal("2.00"))));

        when(orderService.getOrder("order-1")).thenReturn(order);

        Order result = controller.getOrder("order-1");

        assertSame(order, result);
        verify(orderService).getOrder("order-1");
    }
}
