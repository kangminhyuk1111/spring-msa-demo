package com.example.orderservice.exception;

public class ProductNotFoundException extends ApplicationException {

  public ProductNotFoundException(String message) {
    super(message);
  }

  public ProductNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
