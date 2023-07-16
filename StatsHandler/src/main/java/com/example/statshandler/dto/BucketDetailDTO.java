package com.example.statshandler.dto;

import com.example.statshandler.domain.Category;
import com.example.statshandler.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketDetailDTO {
    private String title;
    private Long productId;
    private String image;
    private double discountPrice;
    private String description;
    private List<String> category;

    private double price;
    private int amount;
    private double sum;

    public BucketDetailDTO(Product product) {
        this.title = product.getTitle();
        this.productId = product.getId();
        this.description = product.getDescription();

        List<String> categories = new ArrayList<>();
        for (Category c : product.getCategories()){
            categories.add(c.getTitle());
        }

        this.category = categories;
        this.image = product.getImage();
        this.price = product.getPrice();
        this.amount = 1;
        if (product.getDiscount().getDiscount_price() > 0) {
            this.sum = product.getDiscount().getDiscount_price();
        } else {
            this.sum = product.getPrice();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }
}
