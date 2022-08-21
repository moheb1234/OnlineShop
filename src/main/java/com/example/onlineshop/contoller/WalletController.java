package com.example.onlineshop.contoller;

import com.example.onlineshop.model.User;
import com.example.onlineshop.service.WalletService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class WalletController {
    private final WalletService walletService;

    @PutMapping("wallet/deposit")
    public ResponseEntity<Integer> deposit(@AuthenticationPrincipal User user, @RequestParam int amount){
        return ResponseEntity.ok(walletService.deposit(amount,user.getWallet()));
    }
}
