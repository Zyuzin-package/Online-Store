package com.example.diplom.dto;

import com.example.diplom.domain.Order;
import com.example.diplom.domain.OrderDetails;
import com.example.diplom.domain.OrderStatus;
import com.example.diplom.domain.UserM;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Long userId;
    private double sum;
    private String address;
    private String status;

    private List<OrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();

    public void aggregate(){
        this.sum = orderDetailsDTOList.stream()
                .map(OrderDetailsDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderDetailsDTO> getOrderDetailsDTOList() {
        return orderDetailsDTOList;
    }

    public void setOrderDetailsDTOList(List<OrderDetailsDTO> orderDetailsDTOList) {
        this.orderDetailsDTOList = orderDetailsDTOList;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", created=" + created +
                ", updated=" + updated +
                ", userId=" + userId +
                ", sum=" + sum +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", orderDetailsDTOList=" + orderDetailsDTOList +
                '}';
    }
}
