package com.example.orderservice.entity;

import com.example.orderservice.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long productId;

  @Column(nullable = false)
  private Integer quantity;

  @Column(nullable = false)
  private Integer totalPrice;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  public Order() {
  }

  public Order(Long productId, Integer quantity, Integer totalPrice) {
    this.productId = productId;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.status = OrderStatus.PENDING;
  }

  public Order(final Long productId, final Integer quantity, final Integer totalPrice,
      final OrderStatus status) {
    this.productId = productId;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.status = status;
  }

  public Order(Long id, Long productId, Integer quantity, Integer totalPrice, OrderStatus status) {
    this.id = id;
    this.productId = productId;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
    this.status = status;
  }

  public void complete() {
    if (this.status != OrderStatus.PENDING) {
      throw new ApplicationException("대기 중인 주문만 완료 처리할 수 있습니다.");
    }
    this.status = OrderStatus.COMPLETED;
  }

  public void cancel() {
    if (this.status != OrderStatus.PENDING) {
      throw new ApplicationException("대기 중인 주문만 취소 처리할 수 있습니다.");
    }
    this.status = OrderStatus.CANCELLED;
  }

  public Long getId() {
    return id;
  }

  public Long getProductId() {
    return productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public Integer getTotalPrice() {
    return totalPrice;
  }

  public OrderStatus getStatus() {
    return status;
  }
}
