package com.IT.service;

import com.IT.exception.RecordAlreadyExistsException;
import com.IT.model.Category;
import com.IT.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService{
    @Autowired
    private CategoryRepository categoryRepository;

    public void createCategory(Category category) {
        Optional<Category> existing = categoryRepository.findByName(category.getName());
        existing.ifPresent(s -> {
            throw new RecordAlreadyExistsException(category.getName(), "Category");
        });
        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Optional<Category> findByName(String name){
        return categoryRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
