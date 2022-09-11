package com.example.onlineshop.service;

import com.example.onlineshop.model.Inventory;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static com.example.onlineshop.ex_handler.ExceptionMessage.productNotFound;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    @SneakyThrows
    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(productNotFound(id)));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product create(Product product) {
         save(product);
         product.getInventory().setProduct(product);
         return save(product);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Integer increaseInventory(long id, int number) {
        Inventory inventory = findById(id).getInventory();
        return inventoryService.increaseInventory(inventory, number);
    }

    public Integer updatePrice(long id, int price) {
        Product product = findById(id);
        product.setPrice(price);
        save(product);
        return price;
    }

    public Product delete(long id) {
        Product product = findById(id);
        productRepository.delete(product);
        return product;
    }
}
