package com.example.productservice.exception;

public class ProductOutOfStockException extends ProductException {

  public ProductOutOfStockException(Integer stock, Integer quantity) {
    super("재고가 부족합니다. 주문 요청 갯수: " + quantity + ", 현재 남은 갯수: " + stock);
  }
}
