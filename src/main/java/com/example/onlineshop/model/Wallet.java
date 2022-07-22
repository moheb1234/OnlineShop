package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Entity
@Table(name = "WALLET", schema = "shop")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    @Pattern(regexp = "\\d{10}")
    private String walletNumber;

    @Min(1)
    @NotNull
    private int balance;

    @OneToOne
    @JsonBackReference
    private User user;

    public Wallet(int balance) {
        this.balance = balance;
        walletNumber  = generationWalletNumber();
    }

    public boolean deposit(int amount) {
        if (amount <= 0)
            return false;
        balance += amount;
        return true;
    }

    public boolean withdraw(int amount) {
        if (amount <= 0)
            return false;
        balance -= amount;
        return true;
    }

    public String generationWalletNumber() {
        int random = 0;
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            random = (int) (Math.random() * (10));
            number.append(random);
        }
        return number.toString();
    }
}
