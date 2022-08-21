package com.example.onlineshop.contoller;

import com.example.onlineshop.model.Transaction;
import com.example.onlineshop.model.User;
import com.example.onlineshop.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("order/bye-products")
    public ResponseEntity<Transaction> order(@AuthenticationPrincipal User user , @RequestParam String explains){
        return ResponseEntity.ok(orderService.order(user,explains));
    }
}
