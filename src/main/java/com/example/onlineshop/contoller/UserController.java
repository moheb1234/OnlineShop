package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Cart;
import com.example.onlineshop.model.Transaction;
import com.example.onlineshop.model.User;
import com.example.onlineshop.security.LoginResponse;
import com.example.onlineshop.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @GetMapping("user/all")
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("user/find-by-email")
    public ResponseEntity<User> findByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping("user/cart")
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userService.getCart(user));
    }

    @PostMapping(value = "signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
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

    @GetMapping("user/transactions")
    public ResponseEntity<List<Transaction>> transactions(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(userService.getAllTransactions(user));
    }
}
