package com.example.onlineshop.service;

import com.example.onlineshop.ex_handler.ExceptionMessage;
import com.example.onlineshop.model.Wallet;
import com.example.onlineshop.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    public List<Wallet> findAll() {
        return walletRepository.findAll();
    }

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public Integer deposit(int amount, Wallet wallet) {
        if (wallet.deposit(amount)) {
            save(wallet);
            return wallet.getBalance();
        }
        throw new IllegalArgumentException(ExceptionMessage.notValidAmount(amount));
    }

    public void withdraw(int amount, Wallet wallet) {
        if (wallet.withdraw(amount))
            save(wallet);
        else
            throw new IllegalArgumentException(ExceptionMessage.notValidAmount(amount));
    }
}
