package com.example.onlineshop.contoller;

import com.example.onlineshop.dto.ProductFilterRequest;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.service.ProductFiltersService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductFiltersController {
    private final ProductFiltersService productFiltersService;

    @GetMapping("products/filters")
    public List<Product> filters(@RequestBody ProductFilterRequest request) {
        return productFiltersService.filters(request);
    }
}
