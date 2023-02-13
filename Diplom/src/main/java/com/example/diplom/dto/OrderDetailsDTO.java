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
    private Long orderId;
    private Long productId;
    private String productName;
    private BigDecimal amount;
    private BigDecimal price;
    private double sum;

    public void aggregate() {
        this.sum =  price.multiply(amount).doubleValue();
    }

}
