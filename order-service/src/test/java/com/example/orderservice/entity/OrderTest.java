package com.example.orderservice.entity;

import com.example.orderservice.exception.ApplicationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Order 엔티티 테스트")
class OrderTest {

  @Nested
  @DisplayName("객체 생성 테스트")
  class ObjectCreationTest {

    @Test
    @DisplayName("기본 생성자로 Order 객체를 생성할 수 있다")
    void 기본_생성자로_Order_객체를_생성할_수_있다() {
      Order order = new Order();

      assertThat(order).isNotNull();
      assertThat(order.getId()).isNull();
      assertThat(order.getProductId()).isNull();
      assertThat(order.getQuantity()).isNull();
      assertThat(order.getTotalPrice()).isNull();
      assertThat(order.getStatus()).isNull();
    }

    @Test
    @DisplayName("필수 필드로 Order 객체를 생성할 수 있고 상태는 PENDING이다")
    void 필수_필드로_Order_객체를_생성할_수_있고_상태는_PENDING이다() {
      Long productId = 1L;
      Integer quantity = 2;
      Integer totalPrice = 20000;

      Order order = new Order(productId, quantity, totalPrice);

      assertThat(order.getProductId()).isEqualTo(productId);
      assertThat(order.getQuantity()).isEqualTo(quantity);
      assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
      assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("모든 필드를 지정하여 Order 객체를 생성할 수 있다")
    void 모든_필드를_지정하여_Order_객체를_생성할_수_있다() {
      Long id = 1L;
      Long productId = 2L;
      Integer quantity = 3;
      Integer totalPrice = 30000;
      OrderStatus status = OrderStatus.COMPLETED;

      Order order = new Order(id, productId, quantity, totalPrice, status);

      assertThat(order.getId()).isEqualTo(id);
      assertThat(order.getProductId()).isEqualTo(productId);
      assertThat(order.getQuantity()).isEqualTo(quantity);
      assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
      assertThat(order.getStatus()).isEqualTo(status);
    }

    @Test
    @DisplayName("상태를 지정하여 Order 객체를 생성할 수 있다")
    void 상태를_지정하여_Order_객체를_생성할_수_있다() {
      Long productId = 1L;
      Integer quantity = 2;
      Integer totalPrice = 20000;
      OrderStatus status = OrderStatus.COMPLETED;

      Order order = new Order(productId, quantity, totalPrice, status);

      assertThat(order.getProductId()).isEqualTo(productId);
      assertThat(order.getQuantity()).isEqualTo(quantity);
      assertThat(order.getTotalPrice()).isEqualTo(totalPrice);
      assertThat(order.getStatus()).isEqualTo(status);
    }
  }

  @Nested
  @DisplayName("주문 완료 처리 테스트")
  class CompleteOrderTest {

    private Order pendingOrder;
    private Order completedOrder;
    private Order cancelledOrder;

    @BeforeEach
    void setUp() {
      pendingOrder = new Order(1L, 2, 20000);
      completedOrder = new Order(1L, 2, 20000, OrderStatus.COMPLETED);
      cancelledOrder = new Order(1L, 2, 20000, OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("PENDING 상태의 주문은 완료 처리할 수 있다")
    void PENDING_상태의_주문은_완료_처리할_수_있다() {
      pendingOrder.complete();

      assertThat(pendingOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("COMPLETED 상태의 주문은 완료 처리할 수 없다")
    void COMPLETED_상태의_주문은_완료_처리할_수_없다() {
      assertThatThrownBy(() -> completedOrder.complete())
          .isInstanceOf(ApplicationException.class)
          .hasMessage("대기 중인 주문만 완료 처리할 수 있습니다.");
    }

    @Test
    @DisplayName("CANCELLED 상태의 주문은 완료 처리할 수 없다")
    void CANCELLED_상태의_주문은_완료_처리할_수_없다() {
      assertThatThrownBy(() -> cancelledOrder.complete())
          .isInstanceOf(ApplicationException.class)
          .hasMessage("대기 중인 주문만 완료 처리할 수 있습니다.");
    }
  }

  @Nested
  @DisplayName("주문 취소 처리 테스트")
  class CancelOrderTest {

    private Order pendingOrder;
    private Order completedOrder;
    private Order cancelledOrder;

    @BeforeEach
    void setUp() {
      pendingOrder = new Order(1L, 2, 20000);
      completedOrder = new Order(1L, 2, 20000, OrderStatus.COMPLETED);
      cancelledOrder = new Order(1L, 2, 20000, OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("PENDING 상태의 주문은 취소 처리할 수 있다")
    void PENDING_상태의_주문은_취소_처리할_수_있다() {
      pendingOrder.cancel();

      assertThat(pendingOrder.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("COMPLETED 상태의 주문은 취소 처리할 수 없다")
    void COMPLETED_상태의_주문은_취소_처리할_수_없다() {
      assertThatThrownBy(() -> completedOrder.cancel())
          .isInstanceOf(ApplicationException.class)
          .hasMessage("대기 중인 주문만 취소 처리할 수 있습니다.");
    }

    @Test
    @DisplayName("CANCELLED 상태의 주문은 취소 처리할 수 없다")
    void CANCELLED_상태의_주문은_취소_처리할_수_없다() {
      assertThatThrownBy(() -> cancelledOrder.cancel())
          .isInstanceOf(ApplicationException.class)
          .hasMessage("대기 중인 주문만 취소 처리할 수 있습니다.");
    }
  }

  @Nested
  @DisplayName("Getter 메서드 테스트")
  class GetterMethodTest {

    private Order order;

    @BeforeEach
    void setUp() {
      order = new Order(1L, 100L, 5, 50000, OrderStatus.PENDING);
    }

    @Test
    @DisplayName("모든 필드의 값을 정확히 반환한다")
    void 모든_필드의_값을_정확히_반환한다() {
      assertThat(order.getId()).isEqualTo(1L);
      assertThat(order.getProductId()).isEqualTo(100L);
      assertThat(order.getQuantity()).isEqualTo(5);
      assertThat(order.getTotalPrice()).isEqualTo(50000);
      assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }
  }

  @Nested
  @DisplayName("상태 변화 시나리오 테스트")
  class StatusChangeScenarioTest {

    @Test
    @DisplayName("주문 생성 → 완료 처리 시나리오")
    void 주문_생성_완료_처리_시나리오() {
      Order order = new Order(1L, 3, 30000);
      assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

      order.complete();

      assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("주문 생성 → 취소 처리 시나리오")
    void 주문_생성_취소_처리_시나리오() {
      Order order = new Order(1L, 3, 30000);
      assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

      order.cancel();

      assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("완료된 주문은 더 이상 상태 변경이 불가능하다")
    void 완료된_주문은_더_이상_상태_변경이_불가능하다() {
      Order order = new Order(1L, 3, 30000);
      order.complete();

      assertThatThrownBy(() -> order.cancel())
          .isInstanceOf(ApplicationException.class);

      assertThatThrownBy(() -> order.complete())
          .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("취소된 주문은 더 이상 상태 변경이 불가능하다")
    void 취소된_주문은_더_이상_상태_변경이_불가능하다() {
      Order order = new Order(1L, 3, 30000);
      order.cancel();

      assertThatThrownBy(() -> order.complete())
          .isInstanceOf(ApplicationException.class);

      assertThatThrownBy(() -> order.cancel())
          .isInstanceOf(ApplicationException.class);
    }
  }
}