package com.example.onlineshop.service;

import com.example.onlineshop.ex_handler.ExceptionMessage;
import com.example.onlineshop.model.Favorite;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.FavoriteRepository;
import com.sun.jdi.request.DuplicateRequestException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;

import static com.example.onlineshop.ex_handler.ExceptionMessage.duplicateProduct;
import static com.example.onlineshop.ex_handler.ExceptionMessage.productNotFound;

@Service
@AllArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ProductService productService;

    public Favorite addProduct(Favorite favorite, long productId) {
        Product product = productService.findById(productId);
        if (favorite.getProducts().add(product)) {
            return favoriteRepository.save(favorite);
        }
        throw new DuplicateRequestException(duplicateProduct(product.getName()));
    }

    @SneakyThrows
    public Favorite removeProduct(Favorite favorite, long productId) {
        Product product = productService.findById(productId);
        if (favorite.getProducts().remove(product)) {
            return favoriteRepository.save(favorite);
        }
        throw new InstanceNotFoundException(productNotFound(productId));
    }
}
