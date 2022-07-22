package com.example.onlineshop.service;

import com.example.onlineshop.model.Wallet;
import com.example.onlineshop.repository.WalletRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @SneakyThrows
    public Wallet findById(long id) {
        return walletRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
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
        throw new IllegalArgumentException();
    }

    public void withdraw(int amount, Wallet wallet) {
        if (wallet.withdraw(amount))
            save(wallet);
        throw new IllegalArgumentException();
    }
}
