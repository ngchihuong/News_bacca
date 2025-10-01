package com.newsroom.service;

import com.newsroom.dto.CategoryDTO;

import java.util.List;

public interface ICategoryService {
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(String id, CategoryDTO categoryDTO);
    CategoryDTO getCategoryById(String id);
    CategoryDTO getCategoryBySlug(String slug);
    List<CategoryDTO> getAllCategories();
    List<CategoryDTO> getActiveCategories();
    void deleteCategory(String id);
}

