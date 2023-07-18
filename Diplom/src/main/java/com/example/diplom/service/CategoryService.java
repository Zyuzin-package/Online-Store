package com.example.diplom.service;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAll();

    boolean saveCategory(CategoryDTO categoryDTO);

    boolean removeCategoryByName(String title);

    CategoryDTO getCategoryByName(String title);

}
