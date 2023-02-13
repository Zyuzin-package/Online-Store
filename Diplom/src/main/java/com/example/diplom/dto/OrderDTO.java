package com.example.diplom.dto;

import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import com.example.diplom.domain.OrderStatus;
import com.example.diplom.domain.UserM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long userId;
    private double sum;
    private String address;
    private String status;

    private List<OrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();

    public void aggregate(){
        this.sum = orderDetailsDTOList.stream()
                .map(OrderDetailsDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
