package com.example.onlineshop.contoller;

import com.example.onlineshop.model.User;
import com.example.onlineshop.service.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("email/forget-password")
    public ResponseEntity<String> sendPasswordEmail(@RequestParam String email){
        return ResponseEntity.ok(emailService.sendForgottenPasswordEmail(email));
    }

    @PostMapping("email/resend-verifying-code")
    public ResponseEntity<String> resendPasswordEmail(@RequestParam String email){
        return ResponseEntity.ok(emailService.resendVerifyingEmil(email));
    }
}
