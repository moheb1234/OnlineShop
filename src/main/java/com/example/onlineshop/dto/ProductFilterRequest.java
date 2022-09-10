package com.example.onlineshop.dto;

import lombok.Data;

@Data
public class ProductFilterRequest {
    private String productName;
    private String productCategories;
    private String sortBy;
    private int inventory;
}
