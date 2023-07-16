package com.example.models.dto;

import com.example.models.domain.Discount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiscountDTO {
    private Long id;
    private double discount_price;
    private Long product_id;

    public DiscountDTO(Discount discount) {
        this.id=discount.getId();
        this.discount_price = discount.getDiscount_price();
        this.product_id = discount.getProduct().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    @Override
    public String toString() {
        return "DiscountDTO{" +
                "id=" + id +
                ", discount_price=" + discount_price +
                ", product_id=" + product_id +
                '}';
    }
}
