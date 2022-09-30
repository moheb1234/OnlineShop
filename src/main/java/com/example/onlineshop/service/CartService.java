package com.example.onlineshop.service;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.ProductItem;
import com.example.onlineshop.repository.CartRepository;
import com.example.onlineshop.repository.ProductItemRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

import static com.example.onlineshop.ex_handler.ExceptionMessage.productNotFound;

@Slf4j
@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    private final ProductItemRepository productItemRepository;

    public List<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public void clear(Cart cart) {
        for (ProductItem productItem : cart.getProductItems()) {
            productItem.setBought(true);
            productItemRepository.save(productItem);
        }
        cart.getProductItems().clear();
        cartRepository.save(cart);
    }

    public void delete(Cart cart) {
        cartRepository.delete(cart);
    }

    public Cart addProduct(Cart cart, long productId) {
        Product product = productService.findById(productId);
        cart.addProduct(product);
        return save(cart);
    }

    @SneakyThrows
    public Cart removeProduct(Cart cart, long productID) {
        Product product = productService.findById(productID);
        ProductItem productItem = cart.removeProduct(product);
        if (productItem == null)
            throw new InstanceNotFoundException(productNotFound(productID));
        if (productItem.getNumber()==0){
            cart.getProductItems().remove(productItem);
            save(cart);
            productItemRepository.delete(productItem);
        }
        return cart;
    }
}
