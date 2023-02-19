package com.example.diplom.dto;

import com.example.diplom.domain.Discount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDTO {
    private Long id;
    private BigDecimal discount_price;
    private Long product_id;

    public DiscountDTO(Discount discount) {
        this.id=discount.getId();
        this.discount_price = discount.getDiscount_price();
        this.product_id = discount.getProduct().getId();
    }
}
