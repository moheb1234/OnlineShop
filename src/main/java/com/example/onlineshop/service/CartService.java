package com.example.onlineshop.service;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.CartRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static com.example.onlineshop.ex_handler.ExceptionMessage.PRODUCT_NOT_FOUND;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public void clear(Cart cart) {
        cart.getProducts().clear();
        cartRepository.save(cart);
    }

    public Cart addProduct(Cart cart, long productId) {
        Product product = productService.findById(productId);
        cart.addProduct(product);
        return save(cart);
    }

    @SneakyThrows
    public Cart removeProduct(Cart cart, long productID) {
        Product product = productService.findById(productID);
        if (cart.removeProduct(product)) {
            return save(cart);
        }
        throw new InstanceNotFoundException(PRODUCT_NOT_FOUND);
    }
}
