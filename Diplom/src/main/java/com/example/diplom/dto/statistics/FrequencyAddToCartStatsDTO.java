package com.example.diplom.dto.statistics;

import com.example.diplom.domain.Product;
import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrequencyAddToCartStatsDTO {

    private Long id;

    private Product product;
    private LocalDateTime created;

    public FrequencyAddToCartStatsDTO(FrequencyAddToCartStats frequencyAddToCartStats) {
        this.id = frequencyAddToCartStats.getId();
        this.created = frequencyAddToCartStats.getCreated();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
