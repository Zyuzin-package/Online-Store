package com.example.diplom.serviceImp;

import com.example.diplom.domain.Category;
import com.example.diplom.dto.CategoryDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryServiceImpl categoryService;

    @Test
    void getAll() {
    }

    @Test
    void saveCategory() {
        CategoryDTO category = new CategoryDTO();
        category.setTitle("korka");
        categoryService.saveCategory(category);

        assertEquals("korka",categoryService.getCategoryByName("korka").getTitle());
    }

    @Test
    void removeCategoryByName() {
    }

    @Test
    void getCategoryByName() {


    }
}