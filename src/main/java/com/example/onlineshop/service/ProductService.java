package com.example.onlineshop.service;

import com.example.onlineshop.enums.ProductCategories;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Set;

import static com.example.onlineshop.ex_handler.ExceptionMessage.productNotFound;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @SneakyThrows
    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(productNotFound(id)));
    }

    public List<Product> findAllByName(String productName) {
        return productRepository.findAllByName(productName);
    }

    public List<Product> finAllByCategories(String productCategories) {
        return productRepository.findAllByProductCategories(ProductCategories.valueOf(productCategories.toUpperCase()));
    }

    public List<Product> findAllByCheapest() {
        return productRepository.findAllByOrderByPrice();
    }

    public List<Product> findAllByExpensive() {
        return productRepository.findAllByOrderByPriceDesc();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product create(Product product) {
        product.setInventory(1);
        return save(product);
    }
    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Integer updatePrice(long id, int price) {
        Product product = findById(id);
        product.setPrice(price);
        save(product);
        return price;
    }

    public Integer increaseInventory(long id, int number) {
        Product product = findById(id);
        product.setInventory(product.getInventory() + number);
        save(product);
        return product.getInventory();
    }

    public Product delete(long id) {
        Product product = findById(id);
        productRepository.delete(product);
        return product;
    }

    public void ReduceInventory(Set<Product> products) {
        for (Product product : products) {
            product.setInventory(product.getInventory() - 1);
        }
        productRepository.saveAll(products);
    }
}
