package com.example.onlineshop.service;

import com.example.onlineshop.dto.ProductResponse;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductFiltersService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper = new ModelMapper();

    public List<ProductResponse> filters(String productName, String productCategories, String sortBy, int inventory) {
        Sort sort;
        switch (sortBy) {
            case "Expensive" -> sort = Sort.by(Sort.Direction.DESC, "price");
            case "Cheapest" -> sort = Sort.by(Sort.Direction.ASC, "price");
            case "Newest" -> sort = Sort.by(Sort.Direction.DESC, "createdAt");
            default -> sort = null;
        }
        List<Product> products = productRepository.filtersAll(productCategories.toUpperCase()
                , productName
                , inventory
                , sort);
        return List.of(mapper.map(products, ProductResponse[].class));
    }
}
