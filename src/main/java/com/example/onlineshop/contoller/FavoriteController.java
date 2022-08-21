package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Favorite;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.FavoriteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("favorite/add-product/{productId}")
    public ResponseEntity<Favorite> addProduct(@AuthenticationPrincipal User user, @PathVariable long productId){
        return ResponseEntity.ok(favoriteService.addProduct(user.getFavorite(),productId));
    }

    @DeleteMapping("favorite/remove-product/{productId}")
    public ResponseEntity<Favorite> removeProduct(@AuthenticationPrincipal User user, @PathVariable long productId){
        return ResponseEntity.ok(favoriteService.removeProduct(user.getFavorite(),productId));
    }
}
