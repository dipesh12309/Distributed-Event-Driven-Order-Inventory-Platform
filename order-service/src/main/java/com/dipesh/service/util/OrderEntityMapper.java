package com.dipesh.service.util;

import com.dipesh.model.Order;
import com.dipesh.model.OrderItem;
import com.dipesh.service.entity.OrderEntity;
import com.dipesh.service.entity.OrderItemEntity;

import java.util.List;

public final class OrderEntityMapper
{

    private OrderEntityMapper()
    {
    }

    public static OrderEntity toEntity(Order order)
    {

        OrderEntity entity = new OrderEntity(order.getOrderId(), order.getUserId(), order.getState(), order.getCreatedAt());

        for (OrderItem item : order.getItems())
        {
            entity.addItem(new OrderItemEntity(item.getProductId(), item.getQuantity(), item.getPrice()));
        }

        return entity;
    }

    public static Order toDomain(OrderEntity entity)
    {
        List<OrderItem> items = entity.getItems().stream().map(i -> new OrderItem(i.getProductId(), i.getQuantity(), i.getPrice())).toList();
        return new Order(entity.getCreatedAt(), entity.getOrderId(), entity.getUserId(), items, entity.getState());
    }
}

