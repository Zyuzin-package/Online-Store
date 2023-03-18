package com.example.diplom.service;

import com.example.diplom.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();

    boolean saveCategory(CategoryDTO categoryDTO);

    boolean removeCategoryByName(String title);

    CategoryDTO getCategoryByName(String title);

}
