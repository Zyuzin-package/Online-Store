package com.example.diplom.dto;

import com.example.diplom.domain.Product;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
   private Long id;
   private String title;
   private BigDecimal price;
   private String image;
   private String description;

   public ProductDTO(Product product) {
      this.id = product.getId();
      this.title = product.getTitle();
      this.price = product.getPrice();
      this.image = product.getImage();
      this.description = product.getDescription();
   }
}
