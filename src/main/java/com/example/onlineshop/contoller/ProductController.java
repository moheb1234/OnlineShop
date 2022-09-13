package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Product;
import com.example.onlineshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("products/all")
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @PostMapping("product/create")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return new ResponseEntity<>(productService.create(product), HttpStatus.CREATED);
    }

    @PutMapping("product/update/price/{id}")
    public ResponseEntity<Integer> updatePrice(@PathVariable long id, @RequestParam int price) {
        return ResponseEntity.ok(productService.updatePrice(id, price));
    }

    @PutMapping("product/increase-inventory/{id}")
    public ResponseEntity<Integer> increaseInventory(@RequestParam int number, @PathVariable long id) {
        return ResponseEntity.ok(productService.increaseInventory(id, number));
    }

    @DeleteMapping("product/delete/{id}")
    public ResponseEntity<Product> delete(@PathVariable long id) {
        return ResponseEntity.ok(productService.delete(id));
    }
}
