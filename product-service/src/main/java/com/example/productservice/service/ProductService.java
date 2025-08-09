package com.example.productservice.service;

import com.example.productservice.dto.request.CreateProductRequest;
import com.example.productservice.dto.request.ReduceProductRequest;
import com.example.productservice.dto.request.RestoreProductRequest;
import com.example.productservice.dto.request.UpdateProductRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.exception.ProductNotFoundException;
import com.example.productservice.exception.ProductOutOfStockException;
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
    final Product product = findProductById(id);

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
    final Product product = findProductById(id);

    product.update(request.name(), request.price(), request.stock());

    return ProductResponse.of(product);
  }

  @Transactional
  public void delete(final Long id) {
    productRepository.deleteById(id);
  }

  @Transactional
  public void reduceStock(final ReduceProductRequest request) {
    final Product product = findProductById(request.id());

    if (product.getStock() < request.quantity()) {
      throw new ProductOutOfStockException(product.getStock(), request.quantity());
    }

    product.reduceStock(request.quantity());
  }

  @Transactional
  public void restoreProduct(final RestoreProductRequest request) {
    final Product product = findProductById(request.id());

    product.restoreStock(request.restoreQuantity());
  }

  private Product findProductById(final Long id) {
    return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
  }
}
