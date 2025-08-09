package com.example.orderservice.client;

import com.example.orderservice.dto.request.ReduceProductRequest;
import com.example.orderservice.dto.request.RestoreProductRequest;
import com.example.orderservice.dto.response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-client", url = "${product-service.api-url}")
public interface ProductClient {

  @GetMapping("/{id}")
  ProductResponse getProduct(@PathVariable("id") Long productId);

  @PutMapping("/reduce")
  void reduceStock(@RequestBody ReduceProductRequest request);

  @PutMapping("/restore")
  void restoreStock(@RequestBody RestoreProductRequest request);
}