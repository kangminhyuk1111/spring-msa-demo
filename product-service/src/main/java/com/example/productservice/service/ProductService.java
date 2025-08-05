package com.example.productservice.service;

import com.example.productservice.dto.request.CreateProductRequest;
import com.example.productservice.dto.request.UpdateProductRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private final ProductRepository productRepository;

  public ProductService(final ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAll().stream().map(ProductResponse::of).toList();
  }

  public ProductResponse findById(final Long id) {
    final Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

    return ProductResponse.of(product);
  }

  @Transactional
  public ProductResponse save(final CreateProductRequest request) {
    final Product product = request.toDomain();

    final Product saved = productRepository.save(product);

    return ProductResponse.of(saved);
  }

  @Transactional
  public ProductResponse update(final Long id, final UpdateProductRequest request) {
    final Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("상품이 존재하지 않습니다."));

    product.update(request.name(), request.price(), request.stock());

    return ProductResponse.of(product);
  }

  @Transactional
  public void delete(final Long id) {
    productRepository.deleteById(id);
  }
}
