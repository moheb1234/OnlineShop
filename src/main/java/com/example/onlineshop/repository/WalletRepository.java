package com.example.onlineshop.repository;

import com.example.onlineshop.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
//    Optional<Wallet> findByWalletNumber(String walletNumber);
}
