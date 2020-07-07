package com.IT.service;

import com.IT.exception.RecordAlreadyExistsException;
import com.IT.exception.RecordNotFoundException;
import com.IT.exception.ValidationException;
import com.IT.model.Inventory;
import com.IT.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public void createInventory(Inventory inventory) {
        Optional<Inventory> existing = inventoryRepository.findByName(inventory.getName());
        existing.ifPresent(s -> {
            throw new RecordAlreadyExistsException(inventory.getName(), "Inventory");
        });
        if (inventory.getQuantity() < 0){
            throw new ValidationException("Quantity", "Quantity cannot be negative");
        }
        inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public Optional<Inventory> findByName(String name){
        return inventoryRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Inventory> findAll(){
        return inventoryRepository.findAll();
    }

    public void updateInventory(String name, int quantity){
        Optional<Inventory> inventoryRecord = inventoryRepository.findByName(name);
        Inventory inventory = inventoryRecord.orElseThrow(() -> new RecordNotFoundException(name, "Inventory"));
        int updatedQuantity = inventory.getQuantity() + quantity;
        if (updatedQuantity < 0){
            throw new ValidationException("Quantity", "Updated Quantity cannot be negative");
        }
        inventory.setQuantity(updatedQuantity);
        inventoryRepository.save(inventory);
    }

}
