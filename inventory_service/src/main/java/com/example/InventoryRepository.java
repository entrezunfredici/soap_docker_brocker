package com.example;

import org.springframework.stereotype.Component;

@Component
public class InventoryRepository {

    public boolean checkInventory(String id, int quantity) {
        // Simple règle : si ID = "1", retourne false, sinon true
        return !id.equals("1");
    }
}
