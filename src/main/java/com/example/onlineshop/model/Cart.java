package com.example.onlineshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "CART", schema = "shop")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JsonBackReference
    private User user;

    @OneToMany(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Set<ProductItem> products = new HashSet<>();

    @UpdateTimestamp
    private Date lastModifiedAt;

    public int totalPrice() {
        int total = 0;
        for (ProductItem product : products) {
            total+=product.getNumber();
        }
        return total;
    }

    public void addProduct(Product product) {
        for (ProductItem productItem : products) {
            if (productItem.getProduct().getId()==product.getId()){
                productItem.setNumber(productItem.getNumber()+1);
                return;
            }
        }
        products.add(new ProductItem(product,this,1));
    }

    public boolean removeProduct(Product product) {
        for (ProductItem productItem : products) {
            if (productItem.getProduct().getId()==product.getId()){
                productItem.setNumber(productItem.getNumber()-1);
                return true;
            }
        }
        return false;
    }

    public List<Product> nonExistProducts() {
        return products.stream().map(ProductItem::getProduct).filter(product -> product.getInventory() == 0).toList();
    }
}
