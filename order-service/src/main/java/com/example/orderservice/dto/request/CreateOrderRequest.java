package com.example.orderservice.dto.request;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;

public record CreateOrderRequest(Long productId, Integer quantity) {
  public Order toDomain(Integer price) {
    return new Order(productId, quantity, price * quantity, OrderStatus.PENDING);
  }
}
