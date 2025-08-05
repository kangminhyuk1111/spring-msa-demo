package com.example.productservice.entity;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

  @Test
  void 상품_생성_테스트() {
    String name = "노트북";
    Integer price = 1000000;
    Integer stock = 10;

    Product product = new Product(name, price, stock);

    assertThat(product.getName()).isEqualTo(name);
    assertThat(product.getPrice()).isEqualTo(price);
    assertThat(product.getStock()).isEqualTo(stock);
  }

  @Test
  void 상품_정보_수정_테스트() {
    Product product = new Product("기존상품", 500000, 5);
    String newName = "새로운상품";
    Integer newPrice = 800000;
    Integer newStock = 15;

    product.update(newName, newPrice, newStock);

    assertThat(product.getName()).isEqualTo(newName);
    assertThat(product.getPrice()).isEqualTo(newPrice);
    assertThat(product.getStock()).isEqualTo(newStock);
  }

  @Test
  void ID를_포함한_상품_생성_테스트() {
    Long id = 1L;
    String name = "마우스";
    Integer price = 30000;
    Integer stock = 100;

    Product product = new Product(id, name, price, stock);

    assertThat(product.getId()).isEqualTo(id);
    assertThat(product.getName()).isEqualTo(name);
    assertThat(product.getPrice()).isEqualTo(price);
    assertThat(product.getStock()).isEqualTo(stock);
  }
}