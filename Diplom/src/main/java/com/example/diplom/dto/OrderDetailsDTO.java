package com.example.diplom.dto;

import com.example.diplom.domain.OrderDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsDTO {
    private Long id;
//    private Long orderId;
    private Long productId;
    private BigDecimal amount;
    private BigDecimal price;

    public OrderDetailsDTO(OrderDetails orderDetails) {
        this.id = orderDetails.getId();
//        this.orderId = orderDetails.getOrder().getId();
        this.productId = orderDetails.getProduct().getId();
        this.amount = orderDetails.getAmount();
        this.price = orderDetails.getPrice();
    }
}
