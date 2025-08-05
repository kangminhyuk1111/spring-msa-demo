package com.example.productservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "price")
  private Integer price;

  @Column(name = "stock")
  private Integer stock;

  public Product() {
  }

  public Product(final String name, final Integer price, final Integer stock) {
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public void update(final String name, final Integer price, final Integer stock) {
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public Product(final Long id, final String name, final Integer price, final Integer stock) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getPrice() {
    return price;
  }

  public Integer getStock() {
    return stock;
  }
}
