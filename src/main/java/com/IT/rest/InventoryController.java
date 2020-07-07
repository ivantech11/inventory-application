package com.IT.rest;

import com.IT.dto.CreateInventoryRequestDto;
import com.IT.dto.InventoryDto;
import com.IT.dto.UpdateInventoryQuantityRequestDto;
import com.IT.exception.RecordNotFoundException;
import com.IT.model.Category;
import com.IT.model.SubCategory;
import com.IT.model.Inventory;
import com.IT.service.CategoryService;
import com.IT.service.InventoryService;
import com.IT.service.SubCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory/")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping (consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create New Inventory")
    public ResponseEntity<String> createInventory(@RequestBody @Valid CreateInventoryRequestDto createInventoryRequestDto){
        Optional<Category> categoryRecord = categoryService.findByName(createInventoryRequestDto.getCategory());
        Category category = categoryRecord.orElseThrow(() -> new RecordNotFoundException(createInventoryRequestDto.getCategory(), "Category"));
        Optional<SubCategory> subCategoryRecord = subCategoryService.findByNameAndCategory(createInventoryRequestDto.getSubCategory(), category);
        SubCategory subCategory = subCategoryRecord.orElseThrow(() ->
                new RecordNotFoundException(createInventoryRequestDto.getSubCategory(), createInventoryRequestDto.getCategory()));

        Inventory inventory = new Inventory(
                createInventoryRequestDto.getName(),
                subCategory,
                createInventoryRequestDto.getQuantity()
        );
        inventoryService.createInventory(inventory);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{name}")
                .buildAndExpand(inventory.getName())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping (path = "{name}/updateQuantity/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update quantity of an inventory")
    public ResponseEntity<String> updateInventoryQuantity(@Valid @RequestBody UpdateInventoryQuantityRequestDto updateInventoryQuantityRequestDto){
        inventoryService.updateInventory(updateInventoryQuantityRequestDto.getName(), updateInventoryQuantityRequestDto.getQuantity());
        return ResponseEntity.ok().build();
    }

    @GetMapping (path = "{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Browse an Inventory by Name")
    public ResponseEntity<InventoryDto> findByName(@PathVariable("name") String name){
        Optional<Inventory> inventoryRecord = inventoryService.findByName(name);
        Inventory inventory = inventoryRecord.orElseThrow(() -> new RecordNotFoundException(name, "Inventory"));

        return ResponseEntity.ok(convertToDto(inventory));
    }

    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Browse all Inventory")
    public List<InventoryDto> findAll(){
        return inventoryService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private InventoryDto convertToDto (Inventory inventory){
        return new InventoryDto(
                inventory.getName(),
                inventory.getSubCategory().getCategory().getName(),
                inventory.getSubCategory().getName(),
                inventory.getQuantity()
        );
    }
}