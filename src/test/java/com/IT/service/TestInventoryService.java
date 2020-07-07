package com.IT.service;

import com.IT.exception.RecordAlreadyExistsException;
import com.IT.exception.RecordNotFoundException;
import com.IT.exception.ValidationException;
import com.IT.model.Category;
import com.IT.model.Inventory;
import com.IT.model.SubCategory;
import com.IT.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestInventoryService {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    public void testCreateInventorySuccess(){
        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.empty());

        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);
        inventoryService.createInventory(creamCake);
        verify(inventoryRepository, times(1)).save(creamCake);
    }

    @Test
    public void testCreateInventoryExistsException(){
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);

        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.of(creamCake));

        Exception exception = assertThrows(RecordAlreadyExistsException.class, () -> {
            inventoryService.createInventory(creamCake);
        });

        assertEquals("creamCake already exists in Inventory", exception.getMessage());
        verify(inventoryRepository, times(0)).save(creamCake);
    }

    @Test
    public void testCreateInventoryValidationException(){
        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.empty());
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, -10);

        Exception exception = assertThrows(ValidationException.class, () -> {
            inventoryService.createInventory(creamCake);
        });

        assertEquals("Validation error for Quantity: Quantity cannot be negative", exception.getMessage());
        verify(inventoryRepository, times(0)).save(creamCake);
    }

    @Test
    public void testFindByNameSuccess(){
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);

        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.of(creamCake));

        Optional<Inventory> result = inventoryService.findByName("creamCake");
        assertEquals(true, result.isPresent());
        assertEquals("creamCake", result.get().getName());
        assertEquals(cake, result.get().getSubCategory());
        assertEquals(10, result.get().getQuantity());
    }

    @Test
    public void testFindAllSuccess(){
        List<Inventory> inventories = new ArrayList<>();
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);

        Category clothes = new Category("clothes");
        SubCategory shoe = new SubCategory("shoe", clothes);
        Inventory sportShoe = new Inventory("sportShoe", shoe, 30);

        inventories.add(creamCake);
        inventories.add(sportShoe);

        when(inventoryRepository.findAll()).thenReturn(inventories);

        List<Inventory> results = inventoryService.findAll();
        assertEquals(2, results.size());
        assertEquals("creamCake", results.get(0).getName());
        assertEquals(cake, results.get(0).getSubCategory());
        assertEquals(10, results.get(0).getQuantity());
        assertEquals("sportShoe", results.get(1).getName());
        assertEquals(shoe, results.get(1).getSubCategory());
        assertEquals(30, results.get(1).getQuantity());
    }

    @Test
    public void testUpdateInventoryIncreaseSuccess(){
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);

        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.of(creamCake));

        inventoryService.updateInventory("creamCake", 3);
        assertEquals(13, creamCake.getQuantity());
        verify(inventoryRepository, times(1)).save(creamCake);
    }

    @Test
    public void testUpdateInventoryDecreaseSuccess(){
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);

        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.of(creamCake));

        inventoryService.updateInventory("creamCake", -3);
        assertEquals(7, creamCake.getQuantity());
        verify(inventoryRepository, times(1)).save(creamCake);
    }

    @Test
    public void testUpdateInventoryNotFoundException(){
        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            inventoryService.updateInventory("creamCake", 3);
        });

        assertEquals("creamCake is not found in Inventory", exception.getMessage());
        verify(inventoryRepository, times(0)).save(any(Inventory.class));
    }

    @Test
    public void testUpdateInventoryValidationException(){
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);

        when(inventoryRepository.findByName("creamCake")).thenReturn(Optional.of(creamCake));

        Exception exception = assertThrows(ValidationException.class, () -> {
            inventoryService.updateInventory("creamCake", -20);
        });

        assertEquals("Validation error for Quantity: Updated Quantity cannot be negative", exception.getMessage());
        verify(inventoryRepository, times(0)).save(creamCake);
    }
}
