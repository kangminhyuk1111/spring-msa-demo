package com.example.productservice.repository;

import com.example.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaProductRepository extends ProductRepository, JpaRepository<Product, Long> {

  @Modifying
  @Query("UPDATE Product p SET p.stock = p.stock - :quantity " +
      "WHERE p.id = :id AND p.stock >= :quantity")
  @Override
  int reduceStockIsAvailable(@Param("id") Long id, @Param("quantity") int quantity);
}
