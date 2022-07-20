package com.example.onlineshop.contoller;

import com.example.onlineshop.security.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.Role;
import com.example.onlineshop.model.User;
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
    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("user/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("user/{phoneNumber}")
    public ResponseEntity<User> findByPhoneNumber(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(userService.findByPhoneNumber(phoneNumber));
    }

    @GetMapping("user/cart")
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getCart(user));
    }

    @GetMapping("user/cart/total-price")
    public ResponseEntity<Integer> totalPriceOfCart(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(userService.totalPriceOfCart(user));
    }

    @PostMapping("signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        return new ResponseEntity<>(userService.signup(user), HttpStatus.CREATED);
    }

    @PostMapping("signing")
    public ResponseEntity<LoginResponse> signing(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(userService.signing(username, password,authenticationManager));
    }

    @PostMapping("user/add-product/{productId}")
    public ResponseEntity<Product> addProductToCart(@AuthenticationPrincipal User user, @PathVariable long productId) {
        return ResponseEntity.ok(userService.addToCart(user, productId));
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
