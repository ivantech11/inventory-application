package com.IT.rest;

import com.IT.exception.RecordNotFoundException;
import com.IT.model.Category;
import com.IT.model.SubCategory;
import com.IT.service.CategoryService;
import com.IT.service.SubCategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestSubCategoryController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private SubCategoryService subCategoryService;

    @Test
    public void testCreateSubCategorySuccess() throws Exception{
        when(categoryService.findByName("food")).thenReturn(Optional.of(new Category("food")));

        this.mockMvc.perform(post("/api/subcategory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"cake\", \"category\": \"food\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/api/subcategory/cake"));
    }

    @Test
    public void testCreateSubCategoryEmptyArgumentException() throws Exception{
        this.mockMvc.perform(post("/api/subcategory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("must not be blank"));
    }

    @Test
    public void testCreateSubCategoryCategoryNotFoundException() throws Exception{
        when(categoryService.findByName("food")).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/api/subcategory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"cake\", \"category\": \"food\"}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("food is not found in Category", result.getResolvedException().getMessage()));
    }

    @Test
    public void testFindByNameSuccess() throws Exception{
        when(subCategoryService.findByName("cake")).thenReturn(Optional.of(new SubCategory("cake", new Category("food"))));

        this.mockMvc.perform(get("/api/subcategory/cake")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("cake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("food"));
    }
    @Test
    public void testFindByNameException() throws Exception {
        when(subCategoryService.findByName("shoe")).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/api/subcategory/shoe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("shoe is not found in SubCategory", result.getResolvedException().getMessage()));
    }

    @Test
    public void testFindAllSuccess() throws Exception{
        List<SubCategory> subCategories = new ArrayList<>();
        subCategories.add(new SubCategory("cake", new Category("food")));
        subCategories.add(new SubCategory("shoe", new Category("clothes")));
        when(subCategoryService.findAll()).thenReturn(subCategories);

        this.mockMvc.perform(get("/api/subcategory/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("cake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("food"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].*", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("shoe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].category").value("clothes"));
    }
}
