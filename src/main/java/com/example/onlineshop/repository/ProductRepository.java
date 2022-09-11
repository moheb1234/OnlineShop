package com.example.onlineshop.repository;

import com.example.onlineshop.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where (?1 ='' or p.productCategories = ?1) and (?2='' or lower(p.name) like lower(concat('%',?2,'%'))) and (p.inventory >= ?3)")
    List<Product> filtersAll(String productCategories, String name, int inventory, Sort sort);
}
