package com.example.diplom.service;

import com.example.diplom.dao.CategoryRepository;
import com.example.diplom.domain.Category;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.mapper.CategoryMapper;
import com.example.diplom.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper mapper = CategoryMapper.MAPPER;
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAll() {
        return mapper.fromCategoryList(categoryRepository.findAll());
    }

    @Override
    public CategoryDTO getCategoryByName(String title) {
        Category category = categoryRepository.findByTitle(title);
        return mapper.fromCategory(category);
    }


}
