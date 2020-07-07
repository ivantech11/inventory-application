package com.IT.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "Category", description = "DTO for category information")
public class CategoryDto {

    @Schema(description = "Unique name of the category", required = true)
    @NotBlank
    private String name;

    public CategoryDto() { }

    public CategoryDto(@NotBlank String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}