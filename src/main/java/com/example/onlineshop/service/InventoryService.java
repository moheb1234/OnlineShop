package com.example.onlineshop.service;

import com.example.onlineshop.ex_handler.ExceptionMessage;
import com.example.onlineshop.model.Inventory;
import com.example.onlineshop.model.Product;
import com.example.onlineshop.model.ProductItem;
import com.example.onlineshop.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Min;
import java.util.Set;

@Service
@AllArgsConstructor
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    public Integer increaseInventory(Inventory inventory, int number) {
        if (number<=0){
            throw new IllegalArgumentException(ExceptionMessage.NOT_VALID_NUMBER);
        }
        int increaseNumber = inventory.getNumber() + number;
        inventory.setNumber(increaseNumber);
        inventoryRepository.save(inventory);
        return increaseNumber;
    }

    //after ordering
    public void ReduceInventory(Set<ProductItem> productItems) {
        for (ProductItem productItem : productItems) {
            int numOfOrder = productItem.getNumber();
            Inventory inventory = productItem.getProduct().getInventory();
            inventory.setNumber(inventory.getNumber()-numOfOrder);
            inventoryRepository.save(inventory);
        }
    }
}
