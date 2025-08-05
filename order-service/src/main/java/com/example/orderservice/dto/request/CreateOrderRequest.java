package com.example.orderservice.dto.request;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;

public record CreateOrderRequest(Long productId, Integer quantity, Integer totalPrice) {
  public Order toDomain() { return new Order(productId, quantity, totalPrice, OrderStatus.PENDING); }
}
