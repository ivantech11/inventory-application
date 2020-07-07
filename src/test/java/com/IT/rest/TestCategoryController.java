package com.IT.rest;

import com.IT.exception.RecordNotFoundException;
import com.IT.model.Category;
import com.IT.service.CategoryService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestCategoryController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void testCreateCategorySuccess() throws Exception{
        this.mockMvc.perform(post("/api/category/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"food\"}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/api/category/food"));
    }

    @Test
    public void testCreateCategoryEmptyArgumentException() throws Exception{
        this.mockMvc.perform(post("/api/category/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"));
    }

    @Test
    public void testFindByNameSuccess() throws Exception{
        when(categoryService.findByName("food")).thenReturn(Optional.of(new Category("food")));

        this.mockMvc.perform(get("/api/category/food")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("food"));
    }
    @Test
    public void testFindByNameException() throws Exception {
        when(categoryService.findByName("clothes")).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/api/category/clothes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("clothes is not found in Category", result.getResolvedException().getMessage()));
    }

    @Test
    public void testFindAllSuccess() throws Exception{
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("food"));
        categories.add(new Category("clothes"));
        when(categoryService.findAll()).thenReturn(categories);

        this.mockMvc.perform(get("/api/category/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("food"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("clothes"));
    }
}
