package com.example.onlineshop.service;

import com.example.onlineshop.model.Transaction;
import com.example.onlineshop.repository.TransactionRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @SneakyThrows
    public Transaction findById(long id){
        return transactionRepository.findById(id).orElseThrow(InstanceNotFoundException::new);
    }

    public List<Transaction> findAll(){
        return transactionRepository.findAll();
    }

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }
}
