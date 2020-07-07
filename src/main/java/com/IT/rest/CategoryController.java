package com.IT.rest;

import com.IT.dto.CategoryDto;
import com.IT.dto.CreateCategoryRequestDto;
import com.IT.exception.RecordNotFoundException;
import com.IT.model.Category;
import com.IT.service.CategoryService;
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
@RequestMapping("/api/category/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping (consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create New Category")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CreateCategoryRequestDto createCategoryRequestDto){
        Category category = new Category(
                createCategoryRequestDto.getName()
        );
        categoryService.createCategory(category);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{name}")
                .buildAndExpand(category.getName())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping (path = "{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Browse a category by Name")
    public ResponseEntity<CategoryDto> findByName(@PathVariable("name") String name){
        Optional<Category> categoryRecord = categoryService.findByName(name);
        Category category = categoryRecord.orElseThrow(() -> new RecordNotFoundException(name, "Category"));

        CategoryDto dto = new CategoryDto(
                category.getName()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Browse all Category")
    public List<CategoryDto> findAll(){
        return categoryService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CategoryDto convertToDto (Category category){
        return new CategoryDto(
                category.getName()
        );
    }
}