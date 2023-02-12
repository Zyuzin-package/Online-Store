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
    private BigDecimal sum;
    private String address;
//    private List<Long> detailsIds;
    private String status;

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.created = order.getCreated();
        this.updated = order.getUpdated();
        this.userId = order.getUser().getId();
        this.sum = order.getSum();
        this.address = order.getAddress();
//        List<Long> detailsIds = new ArrayList<>();
//        for (OrderDetails orderDetails : order.getDetails()) {
//            detailsIds.add(orderDetails.getId());
//        }
//        this.detailsIds = detailsIds;
        this.status = order.getStatus().name();
    }
}
