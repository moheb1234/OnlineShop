package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "PRODUCT_ITEM", schema = "shop")
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.REFRESH , fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Product product;

    @ManyToOne
    @JsonBackReference
    private Cart cart;

    @Min(0)
    private int number;

    private boolean bought = false;

    public ProductItem(Product product, Cart cart, int number) {
        this.product = product;
        this.cart = cart;
        this.number = number;
    }
}
