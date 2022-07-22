package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "TRANSACTION",schema = "shop")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @CreationTimestamp
    private Date transactionDate;

    @Min(1)
    private int amount;

    private String explains;

    @ManyToOne
    @JsonBackReference
    private User user;

    public Transaction(int amount, String explains) {
        this.amount = amount;
        this.explains = explains;
    }
}
