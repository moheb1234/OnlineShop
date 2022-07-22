package com.example.onlineshop.service;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

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

}
