package com.example.onlineshop.model;

import com.example.onlineshop.enums.ProductCategories;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "PRODUCT", schema = "shop")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    private String name;

    @Min(1)
    private int price;

    @NotNull
    @NotBlank
    @Column(unique = true)
    private String imageUrl;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private Inventory inventory=new Inventory();

    @Enumerated(EnumType.STRING)
    private ProductCategories productCategories;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date lastModifiedAt;
}
