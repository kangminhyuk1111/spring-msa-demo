package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.request.ReduceProductRequest;
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
  private final ProductClient productClient;

  public OrderService(final OrderRepository orderRepository, final ProductClient productClient) {
    this.orderRepository = orderRepository;
    this.productClient = productClient;
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
    final ProductResponse product = productClient.getProduct(request.productId());

    reduceProductStock(request.productId(), request.quantity());

    final Order order = request.toDomain(product.price());

    final Order saved = orderRepository.save(order);

    return OrderResponse.of(saved);
  }

  @Transactional
  public OrderResponse cancelOrder(final Long id) {
    final Order order = orderRepository.findById(id)
        .orElseThrow(() -> new ApplicationException("주문 정보를 찾을 수 없습니다."));

    order.cancel();

    return OrderResponse.of(order);
  }

  private void reduceProductStock(final Long productId, final Integer quantity) {
    final ReduceProductRequest request = new ReduceProductRequest(productId, quantity);

    productClient.reduceStock(request);
  }
}
