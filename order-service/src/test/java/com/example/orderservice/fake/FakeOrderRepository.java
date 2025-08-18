package com.example.orderservice.fake;

import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderStatus;

import com.example.orderservice.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeOrderRepository implements OrderRepository {

  private final Map<Long, Order> storage = new HashMap<>();
  private Long nextId = 1L;

  @Override
  public List<Order> findAll() {
    return new ArrayList<>(storage.values());
  }

  @Override
  public Optional<Order> findById(Long id) {
    return Optional.ofNullable(storage.get(id));
  }

  @Override
  public Order save(Order order) {
    if (order.getId() == null) {
      // 새로운 엔티티 - ID 생성
      Order newOrder = createOrderWithId(nextId++, order);
      storage.put(newOrder.getId(), newOrder);
      return newOrder;
    } else {
      // 기존 엔티티 - 업데이트
      storage.put(order.getId(), order);
      return order;
    }
  }

  // ID를 가진 새로운 Order 생성 (Order 엔티티가 불변이라고 가정)
  private Order createOrderWithId(Long id, Order order) {
    // Order 생성자가 (memberId, totalPrice, status, orderDate)라고 가정
    // 실제로는 리플렉션이나 빌더 패턴을 사용할 수 있음

    // 임시로 새 Order를 만들고 ID를 설정하는 방식
    // 실제 구현에서는 Order에 setId 메서드가 있거나
    // 다른 방식으로 ID를 설정해야 함

    try {
      Order newOrder = new Order(
          order.getMemberId(),
          order.getTotalPrice(),
          order.getStatus(),
          order.getOrderDate()
      );

      // 리플렉션을 사용해서 ID 설정
      setIdUsingReflection(newOrder, id);
      return newOrder;

    } catch (Exception e) {
      throw new RuntimeException("Order ID 설정 실패", e);
    }
  }

  private void setIdUsingReflection(Order order, Long id) {
    try {
      java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(order, id);
    } catch (Exception e) {
      throw new RuntimeException("리플렉션으로 ID 설정 실패", e);
    }
  }

  // 테스트 헬퍼 메서드들
  public void clear() {
    storage.clear();
    nextId = 1L;
  }

  public int size() {
    return storage.size();
  }

  public boolean exists(Long id) {
    return storage.containsKey(id);
  }

  // 테스트용 데이터 추가 메서드
  public Order saveWithId(Long id, Long memberId, Integer totalPrice, OrderStatus status) {
    Order order = new Order(memberId, totalPrice, status, LocalDateTime.now());
    setIdUsingReflection(order, id);
    storage.put(id, order);
    return order;
  }
}