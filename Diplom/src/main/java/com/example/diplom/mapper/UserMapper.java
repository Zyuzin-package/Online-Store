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
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);
    UserM toProduct(UserDTO dto);
    @InheritInverseConfiguration
    UserDTO fromProduct(UserM product);
    List<UserM> toProductList(List<UserDTO> productDTOList);
    List<UserDTO> fromProductList(List<UserM> products);
}
