package com.example.orderservice.dto.request;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CreateOrderRequestTest {

  @Nested
  @DisplayName("객체 생성 테스트")
  class ObjectCreationTest {

    @Test
    @DisplayName("필수 필드로 CreateOrderRequest를 생성할 수 있다")
    void 필수_필드로_CreateOrderRequest를_생성할_수_있다() {
      Long productId = 1L;
      Integer quantity = 5;

      CreateOrderRequest request = new CreateOrderRequest(productId, quantity);

      assertThat(request.productId()).isEqualTo(productId);
      assertThat(request.quantity()).isEqualTo(quantity);
    }
  }

  @Nested
  @DisplayName("도메인 변환 테스트")
  class ToDomainTest {

    @Test
    @DisplayName("정상적인 값으로 Order 도메인 객체를 생성할 수 있다")
    void 정상적인_값으로_Order_도메인_객체를_생성할_수_있다() {
      CreateOrderRequest request = new CreateOrderRequest(1L, 3);
      Integer price = 10000;

      Order order = request.toDomain(price);

      assertThat(order.getProductId()).isEqualTo(1L);
      assertThat(order.getQuantity()).isEqualTo(3);
      assertThat(order.getTotalPrice()).isEqualTo(30000); // 10000 * 3
      assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("수량이 0일 때 총 가격이 0으로 계산된다")
    void 수량이_0일_때_총_가격이_0으로_계산된다() {
      CreateOrderRequest request = new CreateOrderRequest(1L, 0);
      Integer price = 10000;

      Order order = request.toDomain(price);

      assertThat(order.getQuantity()).isEqualTo(0);
      assertThat(order.getTotalPrice()).isEqualTo(0);
    }

    @Test
    @DisplayName("가격이 0일 때 총 가격이 0으로 계산된다")
    void 가격이_0일_때_총_가격이_0으로_계산된다() {
      CreateOrderRequest request = new CreateOrderRequest(1L, 5);
      Integer price = 0;

      Order order = request.toDomain(price);

      assertThat(order.getQuantity()).isEqualTo(5);
      assertThat(order.getTotalPrice()).isEqualTo(0);
    }
  }
}