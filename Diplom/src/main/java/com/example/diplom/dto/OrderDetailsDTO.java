package com.example.diplom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsDTO {
    private Long id;
    private Long orderId;
    private Long productId;
    private String image;
    private String productName;
    private int amount;
    private double price;
    private double sum;

    public void aggregate(){
        this.sum = price*amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    @Override
    public String toString() {
        return "OrderDetailsDTO{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", image='" + image + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", sum=" + sum +
                '}';
    }
}
