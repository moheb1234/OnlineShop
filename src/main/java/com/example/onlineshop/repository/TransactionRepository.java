package com.example.onlineshop.repository;

import com.example.onlineshop.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction , Long> {
}
