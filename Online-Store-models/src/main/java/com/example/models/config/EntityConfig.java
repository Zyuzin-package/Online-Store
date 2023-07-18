package com.example.models.config;

import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;

import com.example.models.kafka.model.KafkaMessage;
import com.example.models.kafka.model.RequestDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({Bucket.class, Category.class, Discount.class, Order.class, OrderDetails.class, OrderStatus.class, Product.class,
        ProductReview.class, Role.class, UserM.class, UserNotification.class, BuyStats.class, FrequencyAddToCartStats.class, VisitStats.class,

        BucketDetailDTO.class, BucketDTO.class, CategoryDTO.class, DiscountDTO.class, OrderDTO.class, OrderDetailsDTO.class, ProductDTO.class,
        ProductReviewDTO.class, UserDTO.class, UserNotificationDTO.class, BuyStatsDTO.class, FrequencyAddToCartStatsDTO.class, VisitStatsDTO.class,

        RequestDto.class, KafkaMessage.class
})
public class EntityConfig {
}
