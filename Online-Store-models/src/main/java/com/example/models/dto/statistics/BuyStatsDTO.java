package com.example.models.dto.statistics;


import com.example.models.domain.statistics.BuyStats;
import com.example.models.dto.ProductDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class BuyStatsDTO {
    private Long id;
    private ProductDTO product;
    private int amount;
    private LocalDateTime created;

    public BuyStatsDTO(BuyStats buyStats) {
        this.id = buyStats.getId();
        this.amount = buyStats.getAmount();
        this.created = buyStats.getCreated();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
