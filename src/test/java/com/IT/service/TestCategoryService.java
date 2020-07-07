package com.IT.service;

import com.IT.exception.RecordAlreadyExistsException;
import com.IT.model.Category;
import com.IT.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TestCategoryService {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void testCreateCategorySuccess(){
        when(categoryRepository.findByName("food")).thenReturn(Optional.empty());

        Category category = new Category("food");
        categoryService.createCategory(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testCreateCategoryException(){
        Category category = new Category("food");
        when(categoryRepository.findByName("food")).thenReturn(Optional.of(category));

        Exception exception = assertThrows(RecordAlreadyExistsException.class, () -> {
            categoryService.createCategory(category);
        });

        assertEquals("food already exists in Category", exception.getMessage());
        verify(categoryRepository, times(0)).save(category);
    }

    @Test
    public void testFindByNameSuccess(){
        when(categoryRepository.findByName("food")).thenReturn(Optional.of(new Category("food")));

        Optional<Category> result = categoryService.findByName("food");
        assertEquals(true, result.isPresent());
        assertEquals("food", result.get().getName());
    }

    @Test
    public void testFindAllSuccess(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("food"));
        categories.add(new Category("clothes"));
        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> results = categoryService.findAll();
        assertEquals(2, results.size());
        assertEquals("food", results.get(0).getName());
        assertEquals("clothes", results.get(1).getName());
    }
}
