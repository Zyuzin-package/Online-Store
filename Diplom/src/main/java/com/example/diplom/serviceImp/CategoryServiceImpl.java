package com.example.diplom.serviceImp;

import com.example.diplom.dao.CategoryRepository;
import com.example.diplom.domain.Category;
import com.example.diplom.dto.CategoryDTO;
import com.example.diplom.mapper.CategoryMapper;
import com.example.diplom.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper mapper = CategoryMapper.MAPPER;
    private final CategoryRepository categoryRepository;


    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAll() {
        List<Category> categories = categoryRepository.findAll();
        return mapper.fromCategoryList(categories);
    }
    @Override
    public boolean saveCategory(CategoryDTO categoryDTO) {
        if (getCategoryByName(categoryDTO.getTitle()) == null) {
            Category category = Category.builder()
                    .title(categoryDTO.getTitle())
                    .build();
            categoryRepository.save(category);
            return true;
        }
        return false;
    }
    @Override
    public void removeCategoryByName(String title) {
        categoryRepository.removeByTitle(title);
    }

    @Override
    public CategoryDTO getCategoryByName(String title) {
        Category category = categoryRepository.findByTitle(title);
        return mapper.fromCategory(category);
    }


}
