package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TRANSACTION", schema = "shop")
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "TRANSACTION_PRODUCT", schema = "shop",
            joinColumns = @JoinColumn(name = "transaction_id"),
            inverseJoinColumns = @JoinColumn(name = "product_item_id")
    )
    private Set<ProductItem> boughtProducts = new HashSet<>();

    public Transaction(int amount, String explains) {
        this.amount = amount;
        this.explains = explains;
    }
}
