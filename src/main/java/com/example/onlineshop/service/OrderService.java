package com.example.onlineshop.service;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.Transaction;
import com.example.onlineshop.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.onlineshop.ex_handler.ExceptionMessage.EMPTY_CART;
import static com.example.onlineshop.ex_handler.ExceptionMessage.NOT_ENOUGH_BALANCE;

@Service
@AllArgsConstructor
public class OrderService {

    private final ProductService productService;
    private final CartService cartService;
    private final WalletService walletService;

    private final TransactionService transactionService;

    public Transaction order(User user, String explains) {
        Cart userCart = user.getCart();
        List<Product> nonExistProducts = userCart.nonExistProducts();
        if (nonExistProducts.size() != 0)
            throw new IllegalArgumentException(nonExistProducts.get(0).getName() + "  Is Not Exist");
        int totalPrice = userCart.totalPrice();
        if (totalPrice == 0)
            throw new IllegalArgumentException(EMPTY_CART);
        if (totalPrice > user.getWallet().getBalance())
            throw new IllegalArgumentException(NOT_ENOUGH_BALANCE);
        productService.ReduceInventory(userCart.getProducts().keySet());
        cartService.clear(userCart);
        walletService.withdraw(totalPrice, user.getWallet());
        Transaction transaction = new Transaction(totalPrice, explains);
        transaction.setUser(user);
        return transactionService.save(transaction);
    }
}
