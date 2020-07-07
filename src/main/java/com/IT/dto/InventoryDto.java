package com.IT.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(name = "Inventory", description = "DTO for inventory information")
public class InventoryDto {

    @Schema(description = "Unique name of the inventory", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Category of the inventory", required = true)
    @NotBlank
    private String category;

    @Schema(description = "Sub-category of the inventory (In assigned category)", required = true)
    @NotBlank
    private String subCategory;

    @Schema(description = "Quantity of the inventory", required = true)
    @Size()
    private int quantity;

    public InventoryDto(){ }

    public InventoryDto(@NotBlank String name, @NotBlank String category, @NotBlank String subCategory, @Size() int quantity) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
