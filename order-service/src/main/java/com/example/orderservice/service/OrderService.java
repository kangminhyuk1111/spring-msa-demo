package com.example.orderservice.service;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.request.OrderItemRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.response.ProductResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.repository.OrderRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final OrderItemService orderItemService;

  public OrderService(final OrderRepository orderRepository, final OrderItemService orderItemService) {
    this.orderRepository = orderRepository;
    this.orderItemService = orderItemService;
  }

  public List<OrderResponse> findAllOrders() {
    return orderRepository.findAll().stream().map(OrderResponse::of).toList();
  }

  public OrderResponse findOrderById(final Long id) {
    final Order order = orderRepository.findById(id)
        .orElseThrow(() -> new ApplicationException("주문 정보를 찾을 수 없습니다."));

    return OrderResponse.of(order);
  }

  @Transactional
  public OrderResponse createOrder(final CreateOrderRequest request) {
    final List<OrderItemRequest> items = request.items();

    final Integer totalPrice = orderItemService.calculateTotalPrice(items);

    final Order order = request.toDomain(totalPrice);

    final Order savedOrder = orderRepository.save(order);

    orderItemService.saveOrderItems(items, savedOrder);

    return OrderResponse.of(savedOrder);
  }

  @Transactional
  public OrderResponse cancelOrder(final Long orderId) {
    final Order order = orderRepository.findById(orderId)
        .orElseThrow(() -> new ApplicationException("주문 정보를 찾을 수 없습니다."));

    order.cancel();

    return OrderResponse.of(order);
  }
}
