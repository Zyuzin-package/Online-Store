package com.example.diplom.service;

import com.example.diplom.domain.Category;
import com.example.diplom.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();
    CategoryDTO getCategoryByName(String title);

}
