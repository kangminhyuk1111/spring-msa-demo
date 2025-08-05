package com.example.orderservice.service;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public OrderService(final OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  public List<OrderResponse> findAll() {
    return orderRepository.findAll().stream().map(OrderResponse::of).toList();
  }

  public OrderResponse findById(final Long id) {
    final Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("주문 정보를 찾을 수 없습니다."));

    return OrderResponse.of(order);
  }

  public OrderResponse save(final CreateOrderRequest request) {
    final Order order = request.toDomain();

    final Order saved = orderRepository.save(order);

    return OrderResponse.of(saved);
  }
}
