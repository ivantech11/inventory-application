package com.IT.repository;

import com.IT.model.Category;
import com.IT.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {

    Optional<SubCategory> findByName(String name);

    Optional<SubCategory> findByNameAndCategory(String name, Category category);

    @Override
    List<SubCategory> findAll();
}
