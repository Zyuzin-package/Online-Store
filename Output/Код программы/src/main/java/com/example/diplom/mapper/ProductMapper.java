package com.example.diplom.mapper;

import com.example.diplom.domain.Product;
import com.example.diplom.dto.ProductDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);
    Product toProduct(ProductDTO dto);
    @InheritInverseConfiguration
    ProductDTO fromProduct(Product product);
    List<Product> toProductList(List<ProductDTO> productDTOList);
    List<ProductDTO> fromProductList(List<Product> products);
}
