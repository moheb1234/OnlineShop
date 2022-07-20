package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "CART", schema = "shop")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "CART_PRODUCT", schema = "shop",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "Product_id"))
    private List<Product> products = new ArrayList<>();

    public int totalPrice(){
        int total = 0;
        for (Product product : products) {
            total+= product.getPrice();
        }
        return total;
    }
}
