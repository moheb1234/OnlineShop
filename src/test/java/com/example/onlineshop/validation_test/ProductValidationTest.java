package com.example.onlineshop.validation_test;

import com.example.onlineshop.enums.ProductCategories;
import com.example.onlineshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class ProductValidationTest {

    private Product product;
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        product = new Product("product", 1000, "image url", ProductCategories.SPORT);
    }

    //name is empty
    @Test
    void invalidNameEmpty() {
        product.setName("");
        assertProductIsInvalid();
    }

    //price is smaller than 1
    @Test
    void invalidPriceSmall() {
        product.setPrice(0);
        assertProductIsInvalid();
    }

    //image is null
    @Test
    void invalidImageUrlNull() {
        product.setImageUrl(null);
        assertProductIsInvalid();
    }

    void assertProductIsInvalid() {
        Set<ConstraintViolation<Product>> violations = validator.validate(product);
        assertFalse(violations.isEmpty());
    }
}
