package com.example.orderservice.client;

import static org.junit.jupiter.api.Assertions.*;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "product-service.api-url=http://localhost:8082/api/products")
public class ProductClientIntegrationTest {

  @Autowired
  private ProductClient productClient;

  @Test
  public void testGetProduct() {
    assertThrows(FeignException.NotFound.class, () -> {
      productClient.getProduct(1000000L);
    });
  }
}
