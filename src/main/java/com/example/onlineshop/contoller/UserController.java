package com.example.onlineshop.contoller;

import com.example.onlineshop.model.*;
import com.example.onlineshop.security.LoginResponse;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<String> verifyingCode(@RequestParam String verifyingCode){
        return ResponseEntity.ok(userService.verifyingCode(verifyingCode));
    }

    @GetMapping("user/forget-password")
    public ResponseEntity<String> getForgottenPassword(@RequestParam String email){
        return ResponseEntity.ok(userService.sendForgottenPassword(email));
    }

    @PostMapping("signing")
    public ResponseEntity<LoginResponse> signing(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.signing(username, password,authenticationManager));
    }

    @PostMapping("user/add-product/{productId}")
    public ResponseEntity<Product> addProductToCart(@AuthenticationPrincipal User user, @PathVariable long productId) {
        return ResponseEntity.ok(userService.addToCart(user, productId));
    }

    @PutMapping("user/deposit")
    public ResponseEntity<Integer> deposit(@AuthenticationPrincipal User user, @RequestParam int amount){
        return ResponseEntity.ok(userService.deposit(user,amount));
    }

    @PutMapping("user/order")
    public ResponseEntity<Transaction> order(@AuthenticationPrincipal User user , @RequestParam String explains){
        return ResponseEntity.ok(userService.order(user, explains));
    }

    @DeleteMapping("user/remove-product/{productId}")
    public ResponseEntity<Product> removeProductFromCart(@AuthenticationPrincipal User user, @PathVariable long productId) {
        return ResponseEntity.ok(userService.removeFromCart(user, productId));
    }

    @PostMapping("user/add-role/{roleId}")
    public ResponseEntity<Role> addRole(@AuthenticationPrincipal User user, @PathVariable long roleId) {
        return ResponseEntity.ok(userService.addRole(user, roleId));
    }

    @DeleteMapping("user/delete/{id}")
    public ResponseEntity<User> delete(@PathVariable long id) {
        return ResponseEntity.ok(userService.delete(id));
    }
}
