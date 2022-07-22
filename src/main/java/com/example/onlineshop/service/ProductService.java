package com.example.onlineshop.service;

import com.example.onlineshop.enums.ProductCategories;
import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.CartRepository;
import com.example.onlineshop.repository.ProductRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static com.example.onlineshop.ex_handler.ExceptionMessage.PRODUCT_NOT_FOUND;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @SneakyThrows
    public Product findById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new InstanceNotFoundException(PRODUCT_NOT_FOUND));
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

    public Product save(Product product) {
        return productRepository.save(product);
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

    public Product addToCart(Cart cart, long id) {
        Product product = findById(id);
        cart.getProducts().add(product);
        cartRepository.save(cart);
        return product;
    }

    @SneakyThrows
    public Product removeFromCart(Cart cart, long productId) {
        Product product = findById(productId);
        if (cart.getProducts().remove(product)) {
            cartRepository.save(cart);
            return product;
        }
        throw new InstanceNotFoundException(PRODUCT_NOT_FOUND);
    }
}
