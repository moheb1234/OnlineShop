package com.example.onlineshop.service_test;

import com.example.onlineshop.enums.ProductCategories;
import com.example.onlineshop.model.*;
import com.example.onlineshop.repository.*;
import com.example.onlineshop.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OrderTest {

    @MockBean
    WalletRepository walletRepository;
    //
    @MockBean
    ProductItemRepository productItemRepository;
    //
    @MockBean
    CartRepository cartRepository;
    //
    @MockBean
    InventoryRepository inventoryRepository;
    //
    @MockBean
    TransactionRepository transactionRepository;
    @Autowired
    private OrderService orderService;
    private User user;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        user = new User("moheb123", "Password12!", "moheb", "moallem", "1@3", "c", "ad", 24, null, true);
        Wallet wallet = new Wallet(10000);
        Cart cart = new Cart();
        user.setWallet(wallet);
        user.setCart(cart);
        cart.setUser(user);
        wallet.setUser(user);
        product1 = new Product("p1", 1000, "im1", ProductCategories.ELECTRONIC);
        product1.setInventory(new Inventory(10));
        //***
        product2 = new Product("p2", 2000, "im2", ProductCategories.ELECTRONIC);
        product2.setInventory(new Inventory(6));
        //***
        ProductItem productItem1 = new ProductItem(product1, cart, 5);
        ProductItem productItem2 = new ProductItem(product2, cart, 2);
        //***
        Set<ProductItem> productItems = new HashSet<>();
        productItems.add(productItem1);
        productItems.add(productItem2);
        //***
        cart.setProductItems(productItems);
    }

    //order products that not exist
    @Test
    void ShouldThrowIllegalArgumentExceptionNonExist() {
        product1.getInventory().setNumber(0);
        assertThrows(IllegalArgumentException.class, this::order);
    }

    @Test
    void ShouldThrowIllegalArgumentExceptionWhenCartIsEmpty() {
        user.getCart().getProductItems().clear();
        assertThrows(IllegalArgumentException.class, this::order);
    }

    @Test
    void ShouldThrowIllegalArgumentExceptionWhenBalanceIsNotEnough() {
        user.getWallet().setBalance(2000);
        assertThrows(IllegalArgumentException.class, this::order);
    }

    @Test
    void testInventoryAfterOrder() {
        order();
        assertEquals(product1.getInventory().getNumber(), 5);
        assertEquals(product2.getInventory().getNumber(), 4);
    }

    @Test
    void cartShouldBeEmptyAfterOrder() {
        order();
        assertTrue(user.getCart().getProductItems().isEmpty());
    }

    @Test
    void testBalanceAfterOrder() {
        //total price = 5*1000 + 2*2000 = 5000+4000 = 9000
        //balance = 10000-9000 = 1000
        order();
        assertEquals(user.getWallet().getBalance(),1000);
    }

    private void order() {
        orderService.order(user, "order");
    }
}
