package com.example.onlineshop.service_test;

import com.example.onlineshop.enums.ProductCategories;
import com.example.onlineshop.model.Inventory;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.repository.InventoryRepository;
import com.example.onlineshop.repository.ProductRepository;
import com.example.onlineshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.management.InstanceNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ProductServiceTest {

    @MockBean
    ProductRepository productRepository;
    @MockBean
    InventoryRepository inventoryRepository;
    @Autowired
    private ProductService productService;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product("product", 10000, "image url test", ProductCategories.SPORT);
        Inventory inventory = new Inventory();
        inventory.setNumber(10);
        testProduct.setInventory(inventory);
        inventory.setProduct(testProduct);
    }

    // invalid number for increase inventory (number=0)
    @Test
    void shouldThrowIllegalArgumentExceptionInvalidNumber() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        assertThrows(IllegalArgumentException.class, () -> productService.increaseInventory(1, 0));
    }

    // increase inventory for non exist product
    @Test
    void shouldThrowInstanceNotFoundException() {
        assertThrows(InstanceNotFoundException.class, () -> productService.increaseInventory(1, 5));
    }

    // success increase inventory  10+20 =30
    @Test
    void InventoryNumberShouldBe30() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        int number = productService.increaseInventory(1, 20);
        assertEquals(number, 30);
    }

    //update price with invalid price  (price = -5)
    @Test
    void shouldThrowIllegalArgumentExceptionInvalidPrice() {
        assertThrows(IllegalArgumentException.class,() -> productService.updatePrice(1,-5));
    }

    //success updating price to 30000
    @Test
    void PriceShouldBe3000() {
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        productService.updatePrice(1,30000);
        assertEquals(testProduct.getPrice(),30000);
    }
}
