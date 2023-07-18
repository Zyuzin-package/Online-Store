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
public interface OrderDetailsMapper {
    OrderDetailsMapper MAPPER = Mappers.getMapper(OrderDetailsMapper.class);
    OrderDetails toOrderDetails(OrderDetailsDTO dto);
    @InheritInverseConfiguration
    OrderDetailsDTO fromOrderDetails(OrderDetails order);
    List<OrderDetails> toOrderDetailsList(List<OrderDetailsDTO> orderDTOList);
    List<OrderDetailsDTO> fromOrderDetailsList(List<OrderDetails> products);
}
