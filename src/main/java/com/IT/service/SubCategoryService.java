package com.IT.service;

import com.IT.exception.RecordAlreadyExistsException;
import com.IT.model.Category;
import com.IT.model.SubCategory;
import com.IT.repository.SubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubCategoryService {
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public void createSubCategory(SubCategory subCategory){
        Optional<SubCategory> existing = subCategoryRepository.findByName(subCategory.getName());
        existing.ifPresent(s -> {
            throw new RecordAlreadyExistsException(subCategory.getName(), "SubCategory");
        });

        subCategoryRepository.save(subCategory);
    }

    @Transactional(readOnly = true)
    public Optional<SubCategory> findByName(String name){
        return subCategoryRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Optional<SubCategory> findByNameAndCategory(String name, Category category){
        return subCategoryRepository.findByNameAndCategory(name, category);
    }

    @Transactional(readOnly = true)
    public List<SubCategory> findAll(){
        return subCategoryRepository.findAll();
    }
}
