package com.example.models.dto;

import com.example.models.domain.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class ProductDTO {
   @JsonProperty("id")
   private Long id;
   @JsonProperty("title")
   private String title;
   @JsonProperty("price")
   private double price;
   @JsonProperty("image")
   private String image;
   @JsonProperty("description")
   private String description;

   public ProductDTO(Product product) {
      this.id = product.getId();
      this.title = product.getTitle();
      this.price = product.getPrice();
      this.image = product.getImage();
      this.description = product.getDescription();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public String toString() {
      return "{" +
              "\"id\":" + id +
              ", \"title\":\"" + title + '\"' +
              ", \"price\":" + price +
              ", \"image\":\"" + image + '\"' +
              ", \"description\":\"" + description + '\"' +
              '}';
   }
}
