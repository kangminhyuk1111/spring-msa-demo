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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

  private static final Logger log = LoggerFactory.getLogger(ProductService.class);

  private final ProductRepository productRepository;

  public ProductService(final ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public List<ProductResponse> findAll() {
    return productRepository.findAll().stream().map(ProductResponse::of).toList();
  }

  public synchronized ProductResponse findById(final Long id) {
    log.info("[상품조회 시작 - SYNC] 상품ID: {}, 스레드: {}",
        id, Thread.currentThread().getName());

    final Product product = findProductById(id);

    log.info("[상품조회 완료 - SYNC] 상품ID: {}, 상품명: {}, 현재재고: {}, 스레드: {}",
        id, product.getName(), product.getStock(), Thread.currentThread().getName());

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

  public synchronized void reduceStock(final ReduceProductRequest request) {
    log.info("[재고차감 시작 - SYNC] 상품ID: {}, 요청수량: {}, 스레드: {}",
        request.id(), request.quantity(), Thread.currentThread().getName());

    final Product product = findProductById(request.id());
    log.info("[재고차감용 상품조회 완료 - SYNC] 상품ID: {}, 현재재고: {}, 스레드: {}",
        request.id(), product.getStock(), Thread.currentThread().getName());

    if (product.getStock() < request.quantity()) {
      log.warn("[재고부족 - SYNC] 상품ID: {}, 현재재고: {}, 요청수량: {}, 스레드: {}",
          request.id(), product.getStock(), request.quantity(), Thread.currentThread().getName());
      throw new ProductOutOfStockException(product.getStock(), request.quantity());
    }

    product.reduceStock(request.quantity());
    log.info("[재고차감 완료 - SYNC] 상품ID: {}, 차감후재고: {}, 스레드: {}",
        request.id(), product.getStock(), Thread.currentThread().getName());

    productRepository.save(product);
  }

  @Transactional
  public synchronized void restoreProduct(final RestoreProductRequest request) {
    final Product product = findProductById(request.id());

    product.restoreStock(request.restoreQuantity());
  }

  private Product findProductById(final Long id) {
    return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException(id));
  }
}
