package com.example.onlineshop.service;

import com.example.onlineshop.ex_handler.ExceptionMessage;
import com.example.onlineshop.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.onlineshop.ex_handler.ExceptionMessage.EMPTY_CART;
import static com.example.onlineshop.ex_handler.ExceptionMessage.NOT_ENOUGH_BALANCE;

@Service
@AllArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final WalletService walletService;

    private final TransactionService transactionService;

    private final InventoryService inventoryService;

    public Transaction order(User user, String explains) {
        Cart userCart = user.getCart();
        List<Product> nonExistProducts = userCart.nonExistProducts();
        if (nonExistProducts.size() != 0)
            throw new IllegalArgumentException(ExceptionMessage.productNotFound(nonExistProducts.get(0).getId()));
        int totalPrice = userCart.totalPrice();
        if (totalPrice == 0)
            throw new IllegalArgumentException(EMPTY_CART);
        if (totalPrice > user.getWallet().getBalance())
            throw new IllegalArgumentException(NOT_ENOUGH_BALANCE);
        inventoryService.ReduceInventory(userCart.getProductItems());
        Set<ProductItem> boughtProduct = new HashSet<>(userCart.getProductItems());
        cartService.clear(userCart);
        walletService.withdraw(totalPrice, user.getWallet());
        Transaction transaction = new Transaction(totalPrice, explains);
        transaction.setUser(user);
        transaction.setBoughtProducts(boughtProduct);
        return transactionService.save(transaction);
    }
}
