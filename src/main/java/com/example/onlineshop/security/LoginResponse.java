package com.example.onlineshop.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class LoginResponse {
    private String token;

    private Collection<? extends GrantedAuthority> authorities;

    public LoginResponse(String token, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.authorities = authorities;
    }
}
