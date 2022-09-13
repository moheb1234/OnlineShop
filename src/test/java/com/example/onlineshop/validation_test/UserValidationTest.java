package com.example.onlineshop.validation_test;

import com.example.onlineshop.model.User;
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
public class UserValidationTest {

    private User user;
    private Validator validator;

    @BeforeEach
    void setUp() {
        user = new User("moheb73", "Mohebmoallem1234$", "moheb", "moallem"
                , "mohebmoallem@gamil.com", "minoo", "address", 25, null, false);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    //username length is smaller than 5
    @Test
    void invalidUsernameWithSmallLength() {
        user.setUsername("mo");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    //username length is bigger than 20
    @Test
    void invalidUsernameWithBigLength() {
        user.setUsername("moefnl3493444jsdjfpw943f");
        assertUserIsInvalid();
    }

    //username contains special character
    @Test
    void invalidUsernameSpecialCharacters() {
        user.setUsername("moheb#45");
        assertUserIsInvalid();
    }

    //password length is smaller than 8
    @Test
    void invalidPasswordSmallLength() {
        user.setPassword("Pass@1");
    }

    //password not contains special character
    @Test
    void invalidPasswordSpecialCharacter() {
        user.setPassword("Password123");
        assertUserIsInvalid();
    }

    //password not contains capital letter
    @Test
    void invalidPasswordCapitalLetter() {
        user.setPassword("password1@2$");
        assertUserIsInvalid();
    }

    //password not contains small letter
    @Test
    void invalidPasswordSmallLetter() {
        user.setPassword("PASSWORD@12%");
        assertUserIsInvalid();
    }

    //password not contains digits
    @Test
    void invalidPasswordNotContainsDigits() {
        user.setPassword("Password@#dS");
        assertUserIsInvalid();
    }

    //firstname contains digits
    @Test
    void invalidFirstNameContainsDigits() {
        user.setFirstname("moheb3");
        assertUserIsInvalid();
    }

    //firstname length not between 3 and 10
    @Test
    void invalidFirstnameLength() {
        user.setFirstname("mo");
        assertUserIsInvalid();
    }

    //firstname contains special character
    @Test
    void invalidFirstnameSpecialChar() {
        user.setFirstname("moheb$");
        assertUserIsInvalid();
    }

    //age is bigger than 100
    @Test
    void invalidAgeBig() {
        user.setAge(101);
        assertUserIsInvalid();
    }

    //age is smaller than 8
    @Test
    void invalidAgeSmall() {
        user.setAge(7);
        assertUserIsInvalid();
    }

    void assertUserIsInvalid() {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

}
