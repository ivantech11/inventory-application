package com.IT.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Error", description = "DTO for error message")
public class ErrorDto {

    @Schema(description = "Error message", required = true)
    private String message;

    public ErrorDto() { }

    public ErrorDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}