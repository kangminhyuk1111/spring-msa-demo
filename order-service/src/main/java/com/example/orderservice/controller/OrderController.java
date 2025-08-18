package com.example.orderservice.controller;

import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.service.OrderService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(final OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public List<OrderResponse> findAllOrders() {
    return orderService.findAllOrders();
  }

  @GetMapping("/{id}")
  public OrderResponse findOrderById(@PathVariable final Long id) {
    return orderService.findOrderById(id);
  }

  @PostMapping
  public OrderResponse createOrder(@RequestBody final CreateOrderRequest request) {
    return orderService.createOrder(request);
  }

  @PutMapping("/{id}/cancel")
  public OrderResponse cancelOrder(@PathVariable final Long id) {
    return orderService.cancelOrder(id);
  }

  @PutMapping("/{id}/payment")
  public OrderResponse paymentOrder(@PathVariable final Long id) {
    return orderService.processOrderPayment(id);
  }
}
