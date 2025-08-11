package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository {
  Product save(Product product);
  Optional<Product> findById(Long id);
  List<Product> findAll();
  void deleteById(Long id);

  @Modifying
  @Query("UPDATE Product p SET p.stock = p.stock - :quantity " +
      "WHERE p.id = :id AND p.stock >= :quantity")
  int decreaseStockAtomically(@Param("id") Long id, @Param("quantity") int quantity);
}
