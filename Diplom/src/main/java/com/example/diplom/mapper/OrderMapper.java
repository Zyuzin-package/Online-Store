package com.example.diplom.mapper;

import com.example.diplom.domain.Order;
import com.example.diplom.dto.OrderDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDTO dto);

    @InheritInverseConfiguration
    OrderDTO fromOrder(Order order);

    List<Order> toOrderList(List<OrderDTO> orderDTOList);

    List<OrderDTO> fromOrderList(List<Order> products);
}
