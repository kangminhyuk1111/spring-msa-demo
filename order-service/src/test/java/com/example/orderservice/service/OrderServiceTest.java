package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.request.CreateOrderRequest;
import com.example.orderservice.dto.response.OrderResponse;
import com.example.orderservice.dto.response.ProductResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;
import com.example.orderservice.exception.ApplicationException;
import com.example.orderservice.exception.ProductNotFoundException;
import com.example.orderservice.repository.OrderRepository;
import feign.FeignException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 테스트")
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private ProductClient productClient;

  @InjectMocks
  private OrderService orderService;

  @Nested
  @DisplayName("전체 주문 조회 테스트")
  class FindAllOrdersTest {

    @Test
    @DisplayName("모든 주문을 조회할 수 있다")
    void 모든_주문을_조회할_수_있다() {
      List<Order> orders = List.of(
          new Order(1L, 1L, 2, 20000, OrderStatus.PENDING),
          new Order(2L, 2L, 1, 15000, OrderStatus.COMPLETED)
      );
      given(orderRepository.findAll()).willReturn(orders);

      List<OrderResponse> result = orderService.findAllOrders();

      assertThat(result).hasSize(2);
      assertThat(result.get(0).id()).isEqualTo(1L);
      assertThat(result.get(1).id()).isEqualTo(2L);
    }

    @Test
    @DisplayName("주문이 없을 때, 빈 리스트를 반환한다.")
    void 주문이_없을_때_빈_리스트를_반환한다() {
      given(orderRepository.findAll()).willReturn(List.of());

      final List<OrderResponse> result = orderService.findAllOrders();

      assertThat(result).hasSize(0);
    }
  }

  @Nested
  @DisplayName("주문 단건 조회 테스트")
  class FindOrderByIdTest {

    @Test
    @DisplayName("존재하는 주문 ID로 주문을 조회할 수 있다")
    void 존재하는_주문_ID로_주문을_조회할_수_있다() {
      Long orderId = 1L;
      Order order = new Order(orderId, 1L, 2, 20000, OrderStatus.PENDING);
      given(orderRepository.findById(orderId)).willReturn(Optional.of(order));

      OrderResponse result = orderService.findOrderById(orderId);

      assertThat(result.id()).isEqualTo(orderId);
      assertThat(result.productId()).isEqualTo(1L);
      assertThat(result.quantity()).isEqualTo(2);
      assertThat(result.totalPrice()).isEqualTo(20000);
      assertThat(result.status()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("존재하지 않는 주문 ID로 조회 시 예외가 발생한다")
    void 존재하지_않는_주문_ID로_조회_시_예외가_발생한다() {
      Long orderId = 999L;
      given(orderRepository.findById(orderId)).willReturn(Optional.empty());

      assertThatThrownBy(() -> orderService.findOrderById(orderId))
          .isInstanceOf(ApplicationException.class);
    }
  }

  @Nested
  @DisplayName("주문 생성 테스트")
  class CreateOrderTest {

    private ProductResponse productResponse;
    private CreateOrderRequest createOrderRequest;

    @BeforeEach
    void setUp() {
      productResponse = new ProductResponse(1L, "테스트 상품", 10000, 100);
      createOrderRequest = new CreateOrderRequest(1L, 5);
    }

    @Test
    @DisplayName("유효한 요청으로 주문을 생성할 수 있다")
    void 유효한_요청으로_주문을_생성할_수_있다() {
      given(productClient.getProduct(1L)).willReturn(productResponse);
      Order savedOrder = new Order(1L, 1L, 5, 50000, OrderStatus.PENDING);
      given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

      OrderResponse result = orderService.createOrder(createOrderRequest);

      assertThat(result.id()).isEqualTo(1L);
      assertThat(result.productId()).isEqualTo(1L);
      assertThat(result.quantity()).isEqualTo(5);
      assertThat(result.totalPrice()).isEqualTo(50000);
      assertThat(result.status()).isEqualTo(OrderStatus.PENDING);
      verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("재고가 부족한 경우 예외가 발생한다")
    void 재고가_부족한_경우_예외가_발생한다() {
      ProductResponse insufficientStockProduct = new ProductResponse(1L, "테스트 상품", 10000, 3);
      CreateOrderRequest requestMoreThanStock = new CreateOrderRequest(1L, 5);
      given(productClient.getProduct(1L)).willReturn(insufficientStockProduct);

      assertThatThrownBy(() -> orderService.createOrder(requestMoreThanStock))
          .isInstanceOf(ApplicationException.class)
          .hasMessage("주문하려는 상품의 재고가 부족합니다. 남은 재고: 3");
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 주문 생성 시 예외가 발생한다")
    void 존재하지_않는_상품으로_주문_생성_시_예외가_발생한다() {
      given(productClient.getProduct(anyLong())).willThrow(FeignException.class);

      assertThatThrownBy(() -> orderService.createOrder(createOrderRequest))
          .isInstanceOf(ProductNotFoundException.class)
          .hasMessage("주문하려는 상품을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("재고와 주문 수량이 같을 때 주문을 생성할 수 있다")
    void 재고와_주문_수량이_같을_때_주문을_생성할_수_있다() {
      ProductResponse exactStockProduct = new ProductResponse(1L, "테스트 상품", 10000, 5);
      CreateOrderRequest exactQuantityRequest = new CreateOrderRequest(1L, 5);
      given(productClient.getProduct(1L)).willReturn(exactStockProduct);
      Order savedOrder = new Order(1L, 1L, 5, 50000, OrderStatus.PENDING);
      given(orderRepository.save(any(Order.class))).willReturn(savedOrder);

      OrderResponse result = orderService.createOrder(exactQuantityRequest);

      assertThat(result).isNotNull();
      assertThat(result.quantity()).isEqualTo(5);
    }
  }

  @Nested
  @DisplayName("주문 취소 테스트")
  class CancelOrderTest {

    @Test
    @DisplayName("PENDING 상태의 주문을 취소할 수 있다")
    void PENDING_상태의_주문을_취소할_수_있다() {
      Long orderId = 1L;
      Order pendingOrder = new Order(orderId, 1L, 2, 20000, OrderStatus.PENDING);
      given(orderRepository.findById(orderId)).willReturn(Optional.of(pendingOrder));

      OrderResponse result = orderService.cancelOrder(orderId);

      assertThat(result.id()).isEqualTo(orderId);
      assertThat(result.status()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("존재하지 않는 주문 취소 시 예외가 발생한다")
    void 존재하지_않는_주문_취소_시_예외가_발생한다() {
      Long orderId = 999L;
      given(orderRepository.findById(orderId)).willReturn(Optional.empty());

      assertThatThrownBy(() -> orderService.cancelOrder(orderId))
          .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("COMPLETED 상태의 주문 취소 시 예외가 발생한다")
    void COMPLETED_상태의_주문_취소_시_예외가_발생한다() {
      Long orderId = 1L;
      Order completedOrder = new Order(orderId, 1L, 2, 20000, OrderStatus.COMPLETED);
      given(orderRepository.findById(orderId)).willReturn(Optional.of(completedOrder));

      assertThatThrownBy(() -> orderService.cancelOrder(orderId))
          .isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("이미 취소된 주문 취소 시 예외가 발생한다")
    void 이미_취소된_주문_취소_시_예외가_발생한다() {
      Long orderId = 1L;
      Order cancelledOrder = new Order(orderId, 1L, 2, 20000, OrderStatus.CANCELLED);
      given(orderRepository.findById(orderId)).willReturn(Optional.of(cancelledOrder));

      assertThatThrownBy(() -> orderService.cancelOrder(orderId))
          .isInstanceOf(ApplicationException.class);
    }
  }
}