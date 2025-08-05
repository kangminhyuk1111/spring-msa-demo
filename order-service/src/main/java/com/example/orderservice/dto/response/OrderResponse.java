package com.example.orderservice.dto.response;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;

public record OrderResponse(
    Long id,
    Long productId,
    Integer quantity,
    Integer totalPrice,
    OrderStatus status
) {
  public static OrderResponse of(Order order) {
    return new OrderResponse(
        order.getId(),
        order.getProductId(),
        order.getQuantity(),
        order.getTotalPrice(),
        order.getStatus()
    );
  }
}
