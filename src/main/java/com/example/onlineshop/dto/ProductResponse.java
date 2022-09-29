package com.example.onlineshop.dto;

import com.example.onlineshop.enums.ProductCategories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private long id;

    private String name;

    private int price;

    private String imageUrl;

    private ProductCategories productCategories;
}
