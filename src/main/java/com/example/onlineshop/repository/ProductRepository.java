package com.example.onlineshop.repository;

import com.example.onlineshop.enums.ProductCategories;
import com.example.onlineshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByName(String name);

    List<Product> findAllByProductCategories(ProductCategories productCategories);

    List<Product> findAllByOrderByPrice();

    List<Product> findAllByOrderByPriceDesc();
}
