package com.IT.rest;

import com.IT.dto.CreateSubCategoryRequestDto;
import com.IT.dto.SubCategoryDto;
import com.IT.exception.RecordNotFoundException;
import com.IT.model.Category;
import com.IT.model.SubCategory;
import com.IT.service.CategoryService;
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
@RequestMapping("/api/subcategory/")
public class SubCategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @PostMapping (consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Create New Sub-Category")
    public ResponseEntity<String> createCategory(@Valid @RequestBody CreateSubCategoryRequestDto createSubCategoryRequestDto){
        Optional<Category> categoryRecord = categoryService.findByName(createSubCategoryRequestDto.getCategory());
        Category category = categoryRecord.orElseThrow(() -> new RecordNotFoundException(createSubCategoryRequestDto.getCategory(), "Category"));
        SubCategory subCategory = new SubCategory(
                createSubCategoryRequestDto.getName(),
                category
        );
        subCategoryService.createSubCategory(subCategory);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{name}")
                .buildAndExpand(subCategory.getName())
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping (path = "{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Browse a sub-category by Name")
    public ResponseEntity<SubCategoryDto> findByName(@PathVariable("name") String name){
        Optional<SubCategory> subCategoryRecord = subCategoryService.findByName(name);
        SubCategory subCategory = subCategoryRecord.orElseThrow(() -> new RecordNotFoundException(name, "SubCategory"));

        SubCategoryDto dto = new SubCategoryDto(
                subCategory.getName(),
                subCategory.getCategory().getName()
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Browse all SubCategory")
    public List<SubCategoryDto> findAll(){
        return subCategoryService.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private SubCategoryDto convertToDto (SubCategory subCategory){
        return new SubCategoryDto(
                subCategory.getName(),
                subCategory.getCategory().getName()
        );
    }
}