package com.example.onlineshop.service_test;

import com.example.onlineshop.model.User;
import com.example.onlineshop.repository.UserRepository;
import com.example.onlineshop.dto.LoginResponse;
import com.example.onlineshop.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;

import javax.management.InstanceNotFoundException;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;
    @Autowired
    private UserService userService;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("moheb73", "Mohebmoallem1234$", "moheb", "moallem"
                , "mohebmoallem@gamil.com", "minoo", "address", 25, null, true);
    }

    //signing with non exist username
    @Test
    void shouldThrowInstanceNotFoundExceptionNonExistUser() {
        Assertions.assertThrows(InstanceNotFoundException.class, () -> {
            userService.signing("ali12", "Password12$");
        });
    }

    //signing with wrong password
    @Test
    void shouldThrowIllegalArgumentExceptionWrongPassword() {
        Mockito.when(userRepository.findByUsername("moheb73")).thenReturn(Optional.ofNullable(user));
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.signing("moheb73", "Password12$");
        });
    }

    //signing with disable user
    @Test
    void shouldThrowIllegalArgumentExceptionDisableUser() {
        user.setEnabled(false);
        Mockito.when(userRepository.findByUsername("moheb73")).thenReturn(Optional.ofNullable(user));
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.signing("moheb73", "Password12$");
        });
    }

    //success signing
    @Test
    void testSuccessSigning() {
        Mockito.when(userRepository.findByUsername("moheb73")).thenReturn(Optional.ofNullable(user));
        LoginResponse response = userService.signing("moheb73", "Mohebmoallem1234$");
        Assertions.assertFalse(response.getToken().isEmpty());
    }

    // test verifying code with not valid code
    @Test
    void shouldThrowIllegalArgumentExceptionInvalidCode() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.verifyingCode("346246"));
    }

    //success verifying code
    @Test
    void testSuccessVerifyingCode() {
        String verifyCode = "354623";
        user.setVerifyingCode(verifyCode);
        user.setEnabled(false);
        Mockito.when(userRepository.findByVerifyingCode(verifyCode)).thenReturn(user);
        userService.verifyingCode(verifyCode);
        Assertions.assertNull(user.getVerifyingCode());
        Assertions.assertTrue(user.isEnabled());
    }
}
