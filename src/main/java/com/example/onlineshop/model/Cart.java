package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CART_PRODUCT", schema = "shop",
            joinColumns = @JoinColumn(name = "cart_id"))
    @Column(name = "product_number")
    @MapKeyJoinColumn(name = "product_id")
    private Map<Product, Integer> products = new HashMap<>();

    @UpdateTimestamp
    private Date lastModifiedAt;

    public int totalPrice() {
        int total = 0;
        for (Product product : products.keySet()) {
            int number = products.get(product);
            total += product.getPrice() * number;
        }
        return total;
    }

    public void addProduct(Product product) {
        if (products.containsKey(product)) {
            products.put(product, products.get(product) + 1);
            return;
        }
        products.put(product, 1);
    }

    public boolean removeProduct(Product product) {
        if (products.containsKey(product)) {
            products.put(product, products.get(product) - 1);
            return true;
        }
        return false;
    }

    public List<Product> nonExistProducts() {
        return products.keySet().stream().filter(product -> product.getInventory() == 0).toList();
    }
}
