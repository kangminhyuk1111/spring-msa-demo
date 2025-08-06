package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.response.ProductResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ProductNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import feign.FeignException;
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

  public List<OrderResponse> findAll() {
    return orderRepository.findAll().stream().map(OrderResponse::of).toList();
  }

  public OrderResponse findById(final Long id) {
    final Order order = orderRepository.findById(id)
        .orElseThrow(() -> new ApplicationException("주문 정보를 찾을 수 없습니다."));

    return OrderResponse.of(order);
  }

  @Transactional
  public OrderResponse save(final CreateOrderRequest request) {
    final ProductResponse product = getProductDetails(request.productId());

    if (product.stock() < request.quantity()) {
      throw new ApplicationException("주문하려는 상품의 재고가 부족합니다. 남은 재고: " + product.stock());
    }

    final Order order = request.toDomain(product.price());

    final Order saved = orderRepository.save(order);

    return OrderResponse.of(saved);
  }

  @Transactional
  public OrderResponse cancel(final Long id) {
    final Order order = orderRepository.findById(id)
        .orElseThrow(() -> new ApplicationException("주문 정보를 찾을 수 없습니다."));

    order.cancel();

    return OrderResponse.of(order);
  }

  private ProductResponse getProductDetails(final Long productId) {
    try {
      return productClient.getProduct(productId);
    } catch (FeignException e) {
      throw new ProductNotFoundException("주문하려는 상품을 찾을 수 없습니다.");
    }
  }
}
