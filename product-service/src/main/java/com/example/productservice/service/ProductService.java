package com.example.productservice.service;

import com.example.productservice.dto.request.CreateProductRequest;
import com.example.productservice.dto.request.UpdateProductRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.repository.ProductRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public ProductService(final ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductResponse> findAll() {
    logger.info("ProductService.findAll() called");
    return productRepository.findAll().stream().map(ProductResponse::of).toList();
  }

  public ProductResponse findById(final Long id) {
    logger.info("ProductService.findById() called");

    final Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));

    return ProductResponse.of(product);
  }

  @Transactional
  public ProductResponse save(final CreateProductRequest request) {
    logger.info("ProductService.save() called");

    final Product product = request.toDomain();

    final Product saved = productRepository.save(product);

    return ProductResponse.of(saved);
  }

  @Transactional
  public ProductResponse update(final Long id, final UpdateProductRequest request) {
    logger.info("ProductService.update() called");

    final Product product = productRepository.findById(id)
        .orElseThrow(() -> new ProductNotFoundException(id));

    product.update(request.name(), request.price(), request.stock());

    return ProductResponse.of(product);
  }

  @Transactional
  public void delete(final Long id) {
    productRepository.deleteById(id);
  }
}
