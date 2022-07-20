package com.example.onlineshop.service;

import lombok.SneakyThrows;
import com.example.onlineshop.model.Cart;
import com.example.onlineshop.repository.CartRepository;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @SneakyThrows
    public Cart findById(long id) {
        return cartRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
    }

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart clear(Cart cart) {
        cart.getProducts().clear();
        return cartRepository.save(cart);
    }

}
