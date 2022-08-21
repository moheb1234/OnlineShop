package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.User;
import com.example.onlineshop.security.LoginResponse;
import com.example.onlineshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("user/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("user/")
    public ResponseEntity<User> findByPhoneNumber(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("user/cart")
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getCart(user));
    }

    @PostMapping("signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return new ResponseEntity<>(userService.signup(user), HttpStatus.CREATED);
    }

    @GetMapping("user/verifying")
    public ResponseEntity<String> verifyingCode(@RequestParam String verifyingCode) {
        return ResponseEntity.ok(userService.verifyingCode(verifyingCode));
    }

    @PostMapping("signing")
    public ResponseEntity<LoginResponse> signing(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.signing(username, password, authenticationManager));
    }

    @DeleteMapping("user/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable long id) {
        return ResponseEntity.ok(userService.delete(id));
    }
}
