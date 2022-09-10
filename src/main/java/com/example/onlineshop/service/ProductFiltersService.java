package com.example.onlineshop.service;

import com.example.onlineshop.dto.ProductFilterRequest;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductFiltersService {
    private final ProductRepository productRepository;

    public List<Product> filters(ProductFilterRequest request) {
        Sort sort;
        switch (request.getSortBy()) {
            case "Expensive" -> sort = Sort.by(Sort.Direction.DESC, "price");
            case "Cheapest" -> sort = Sort.by(Sort.Direction.ASC, "price");
            case "Newest" -> sort = Sort.by(Sort.Direction.DESC, "createdAt");
            default -> sort = null;
        }
        return productRepository.filtersAll(request.getProductCategories().toUpperCase()
                , request.getProductName()
                , request.getInventory()
                , sort);
    }
}
