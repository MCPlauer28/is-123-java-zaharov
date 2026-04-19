package com.curs.blog.service;

import com.curs.blog.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryByName(String name);
    Optional<Category> getCategoryById(Long id);
    Category saveCategory(Category category);
}
