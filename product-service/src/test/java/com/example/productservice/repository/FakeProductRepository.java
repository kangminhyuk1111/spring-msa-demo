package com.example.productservice.repository;

import com.example.productservice.entity.Product;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeProductRepository implements ProductRepository {

  private final Map<Long, Product> store = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  @Override
  public Product save(Product product) {
    if (product.getId() == null) {
      Long newId = idGenerator.getAndIncrement();
      Product newProduct = new Product(newId, product.getName(), product.getPrice(), product.getStock());
      store.put(newId, newProduct);
      return newProduct;
    } else {
      store.put(product.getId(), product);
      return product;
    }
  }

  @Override
  public Optional<Product> findById(Long id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Product> findAll() {
    return new ArrayList<>(store.values());
  }

  @Override
  public void deleteById(Long id) {
    store.remove(id);
  }

  public void clear() {
    store.clear();
    idGenerator.set(1);
  }

  public int size() {
    return store.size();
  }
}