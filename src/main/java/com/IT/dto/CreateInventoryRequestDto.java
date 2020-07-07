package com.IT.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Schema(name = "CreateInventoryRequest", description = "DTO for new inventory request")
public class CreateInventoryRequestDto {

    @Schema(description = "Unique name of the inventory", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Category of the inventory", required = true)
    @NotBlank
    private String category;

    @Schema(description = "Sub-category of the inventory (Required to be in assigned category)", required = true)
    @NotBlank
    private String subCategory;

    @Schema(description = "Non negative quantity of the inventory", required = true)
    @Range(min=0, message = "must not be negative")
    private int quantity = 0;

    public CreateInventoryRequestDto(){ }

    public CreateInventoryRequestDto(@NotBlank String name, @NotBlank String category, @NotBlank String subCategory, @Range() int quantity) {
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
