package com.example.diplom.dto;

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
}
