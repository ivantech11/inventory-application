package com.IT.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "CreateSubCategoryRequest", description = "DTO for new sub-category request")
public class CreateSubCategoryRequestDto {

    @Schema(description = "Unique name of the inventory", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Category of the inventory", required = true)
    @NotBlank
    private String category;

    public CreateSubCategoryRequestDto() { }

    public CreateSubCategoryRequestDto(@NotBlank String name, @NotBlank String category) {
        this.name = name;
        this.category = category;
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
}
