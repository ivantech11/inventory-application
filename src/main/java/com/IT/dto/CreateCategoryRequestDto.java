package com.IT.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

@Schema(name = "CreateCategoryRequest", description = "DTO for new category request")
public class CreateCategoryRequestDto {

    @Schema(description = "Unique name of the category", required = true)
    @NotBlank
    private String name;

    public CreateCategoryRequestDto(){ }

    public CreateCategoryRequestDto(@NotBlank String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}