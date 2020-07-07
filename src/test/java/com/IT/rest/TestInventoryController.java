package com.IT.rest;

import com.IT.exception.RecordNotFoundException;
import com.IT.model.Category;
import com.IT.model.Inventory;
import com.IT.model.SubCategory;
import com.IT.service.CategoryService;
import com.IT.service.InventoryService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TestInventoryController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private SubCategoryService subCategoryService;

    @Test
    public void testCreateInventorySuccess() throws Exception{
        Category food = new Category("food");
        when(categoryService.findByName("food")).thenReturn(Optional.of(food));
        when(subCategoryService.findByNameAndCategory("cake", food)).thenReturn(Optional.of(new SubCategory("cake", food)));

        this.mockMvc.perform(post("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"creamCake\", \"category\": \"food\", \"subCategory\": \"cake\", \"quantity\": 10}"))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.redirectedUrlPattern("**/api/inventory/creamCake"));
    }

    @Test
    public void testCreateInventoryNotHaveCategoryException() throws Exception{
        this.mockMvc.perform(post("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"creamCake\", \"subCategory\": \"cake\", \"quantity\": 10}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("must not be blank"));
    }

    @Test
    public void testCreateInventoryQuantityNotValidException() throws Exception{
        this.mockMvc.perform(post("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"creamCake\", \"category\": \"food\", \"subCategory\": \"cake\", \"quantity\": -10}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value("must not be negative"));
    }

    @Test
    public void testCreateInventoryEmptyArgumentException() throws Exception{
       this.mockMvc.perform(post("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException))
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("must not be blank"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("must not be blank"));
    }

    @Test
    public void testCreateInventoryCategoryNotFoundException() throws Exception{
        when(categoryService.findByName("food")).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"creamCake\", \"category\": \"food\", \"subCategory\": \"cake\", \"quantity\": 10}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("food is not found in Category", result.getResolvedException().getMessage()));
    }

    @Test
    public void testCreateInventorySubCategoryNotFoundInCategoryException() throws Exception{
        Category food = new Category("food");
        when(categoryService.findByName("food")).thenReturn(Optional.of(food));
        when(subCategoryService.findByNameAndCategory("shoe", food)).thenReturn(Optional.empty());

        this.mockMvc.perform(post("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"creamCake\", \"category\": \"food\", \"subCategory\": \"shoe\", \"quantity\": 10}"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("shoe is not found in food", result.getResolvedException().getMessage()));
    }

    @Test
    public void testFindByNameSuccess() throws Exception{
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory inventory = new Inventory("creamCake", cake, 10);
        when(inventoryService.findByName("creamCake")).thenReturn(Optional.of(inventory));

        this.mockMvc.perform(get("/api/inventory/creamCake")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.*", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("creamCake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("food"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subCategory").value("cake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value(10));
    }
    @Test
    public void testFindByNameException() throws Exception {
        when(subCategoryService.findByName("creamCake")).thenReturn(Optional.empty());

        this.mockMvc.perform(get("/api/inventory/creamCake")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof RecordNotFoundException))
                .andExpect(result -> assertEquals("creamCake is not found in Inventory", result.getResolvedException().getMessage()));
    }

    @Test
    public void testFindAllSuccess() throws Exception{
        List<Inventory> inventories = new ArrayList<>();
        Category food = new Category("food");
        SubCategory cake = new SubCategory("cake", food);
        Inventory creamCake = new Inventory("creamCake", cake, 10);

        Category clothes = new Category("clothes");
        SubCategory shoe = new SubCategory("shoe", clothes);
        Inventory sportShoe = new Inventory("sportShoe", shoe, 30);

        inventories.add(creamCake);
        inventories.add(sportShoe);

        when(inventoryService.findAll()).thenReturn(inventories);

        this.mockMvc.perform(get("/api/inventory/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].*", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("creamCake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("food"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].subCategory").value("cake"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].quantity").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].*", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].name").value("sportShoe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].category").value("clothes"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].subCategory").value("shoe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].quantity").value(30));
    }

    @Test
    public void testUpdateInventorySuccess() throws Exception{
        this.mockMvc.perform(patch("/api/inventory/creamCake/updateQuantity/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"creamCake\", \"quantity\": 10}"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }
}
