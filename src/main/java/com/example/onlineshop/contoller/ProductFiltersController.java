package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Product;
import com.example.onlineshop.service.ProductFiltersService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductFiltersController {
    private final ProductFiltersService productFiltersService;

    @GetMapping("products/filters")
    public ResponseEntity<List<Product>> filters(@RequestParam String productCategories
            , @RequestParam String productName
            , @RequestParam int inventory
            , @RequestParam String sortBy) {
        return ResponseEntity.ok(productFiltersService.filters(productCategories, productName, inventory, sortBy));
    }
}
