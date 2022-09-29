package com.example.onlineshop.contoller;

import com.example.onlineshop.dto.ProductResponse;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.service.ProductFiltersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductFiltersController {
    private final ProductFiltersService productFiltersService;

    @GetMapping("products/filters")
    public List<ProductResponse> filters(@RequestParam String productName
            , @RequestParam String productCategories
            , @RequestParam String sortBy
            , @RequestParam int inventory) {
        return productFiltersService.filters(productName, productCategories, sortBy, inventory);
    }
}
