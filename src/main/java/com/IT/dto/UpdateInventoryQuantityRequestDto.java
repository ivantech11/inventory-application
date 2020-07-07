package com.IT.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "UpdateInventoryQuantityRequest", description = "DTO for updating quantity of an inventory request")
public class UpdateInventoryQuantityRequestDto {

    @Schema(description = "Unique name of the inventory", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Quantity update of the inventory. Positive and negative value are for increasing and decreasing stock respectively.", required = true)
    private int quantity = 0;

    public UpdateInventoryQuantityRequestDto(){ }

    public UpdateInventoryQuantityRequestDto(@NotBlank String name, @NotBlank() int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
