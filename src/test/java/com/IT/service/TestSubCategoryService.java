package com.IT.service;

import com.IT.exception.RecordAlreadyExistsException;
import com.IT.model.Category;
import com.IT.model.SubCategory;
import com.IT.repository.SubCategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestSubCategoryService {

    @Mock
    private SubCategoryRepository subCategoryRepository;

    @InjectMocks
    private SubCategoryService subCategoryService;

    @Test
    public void testCreateSubCategorySuccess(){
        when(subCategoryRepository.findByName("cake")).thenReturn(Optional.empty());

        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        subCategoryService.createSubCategory(cake);
        verify(subCategoryRepository, times(1)).save(cake);
    }

    @Test
    public void testCreateSubCategoryException(){
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        when(subCategoryRepository.findByName("cake")).thenReturn(Optional.of(cake));

        Exception exception = assertThrows(RecordAlreadyExistsException.class, () -> {
            subCategoryService.createSubCategory(cake);
        });

        assertEquals("cake already exists in SubCategory", exception.getMessage());
        verify(subCategoryRepository, times(0)).save(cake);
    }

    @Test
    public void testFindByNameSuccess(){
        Category food = new Category("food");
        when(subCategoryRepository.findByName("cake")).thenReturn(Optional.of(new SubCategory("cake", food)));

        Optional<SubCategory> result = subCategoryService.findByName("cake");
        assertEquals(true, result.isPresent());
        assertEquals("cake", result.get().getName());
        assertEquals(food, result.get().getCategory());
    }

    @Test
    public void testFindByNameAndCategorySuccess(){
        Category food = new Category("food");
        when(subCategoryRepository.findByNameAndCategory("cake", food)).thenReturn(Optional.of(new SubCategory("cake", food)));

        Optional<SubCategory> result = subCategoryService.findByNameAndCategory("cake", food);
        assertEquals(true, result.isPresent());
        assertEquals("cake", result.get().getName());
        assertEquals(food, result.get().getCategory());
    }

    @Test
    public void testFindAllSuccess(){
        Category food = new Category("food");
        Category clothes = new Category("clothes");

        List<SubCategory> subCategories = new ArrayList<>();
        subCategories.add(new SubCategory("cake", food));
        subCategories.add(new SubCategory("shoe", clothes));
        when(subCategoryRepository.findAll()).thenReturn(subCategories);

        List<SubCategory> results = subCategoryService.findAll();
        assertEquals(2, results.size());
        assertEquals("cake", results.get(0).getName());
        assertEquals(food, results.get(0).getCategory());
        assertEquals("shoe", results.get(1).getName());
        assertEquals(clothes, results.get(1).getCategory());
    }
}
