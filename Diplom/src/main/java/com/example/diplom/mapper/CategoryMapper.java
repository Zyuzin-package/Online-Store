package com.example.diplom.mapper;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {
    CategoryMapper MAPPER = Mappers.getMapper(CategoryMapper.class);
    Category toCategory(CategoryDTO dto);
    @InheritInverseConfiguration
    CategoryDTO fromCategory(Category category);
    List<Category> toCategoryList(List<CategoryDTO> categoryDTOS);
    List<CategoryDTO> fromCategoryList(List<Category> categories);
}