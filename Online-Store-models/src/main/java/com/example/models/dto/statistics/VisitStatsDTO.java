package com.example.models.dto.statistics;

import com.example.models.dto.ProductDTO;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class VisitStatsDTO {
    private Long id;
    private ProductDTO product;
    private LocalDateTime created;

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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "VisitStatsDTO{" +
                "id=" + id +
                ", product=" + product +
                ", created=" + created +
                '}';
    }
}

