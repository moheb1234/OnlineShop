package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("cart/add-product/{productId}")
    public ResponseEntity<Cart> addProduct(@AuthenticationPrincipal User user , @PathVariable long productId){
        return ResponseEntity.ok(cartService.addProduct(user.getCart(),productId));
    }

    @DeleteMapping("cart/remove-product/{productId}")
    public ResponseEntity<Cart> removeProduct(@AuthenticationPrincipal User user, @PathVariable long productId){
        return ResponseEntity.ok(cartService.removeProduct(user.getCart(),productId));
    }
}
