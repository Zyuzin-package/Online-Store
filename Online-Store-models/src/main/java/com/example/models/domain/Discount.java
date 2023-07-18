package com.example.models.domain;

import lombok.*;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "discount")
public class Discount {
    private static final String SEQ_NAME = "discount_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    private double discount_price;
    @OneToOne
    @JoinColumn(name = "product_id")
    Product product;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
