package com.example.diplom.dao;


import com.example.models.domain.statistics.*;
import com.example.models.dto.statistics.*;
import com.example.models.dto.*;
import com.example.models.domain.*;


import com.example.models.kafka.model.KafkaMessage;
import com.example.models.kafka.model.RequestDto;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@EnableJpaRepositories("com.example.models.domain")
@ComponentScan(basePackages = {"com.example.models.domain"})
@EntityScan(basePackageClasses = {com.example.models.domain.Bucket.class, com.example.models.domain.Category.class, com.example.models.domain.Discount.class, com.example.models.domain.Order.class,
        com.example.models.domain.OrderDetails.class, com.example.models.domain.OrderStatus.class, com.example.models.domain.Product.class,
        com.example.models.domain.ProductReview.class, com.example.models.domain.Role.class, com.example.models.domain.UserM.class,
        com.example.models.domain.UserNotification.class, com.example.models.domain.statistics.BuyStats.class,
        com.example.models.domain.statistics.FrequencyAddToCartStats.class, com.example.models.domain.statistics.VisitStats.class,

        com.example.models.dto.BucketDetailDTO.class, com.example.models.dto.BucketDTO.class, com.example.models.dto.CategoryDTO.class,
        com.example.models.dto.DiscountDTO.class, com.example.models.dto.OrderDTO.class, com.example.models.dto.OrderDetailsDTO.class,
        com.example.models.dto.ProductDTO.class, com.example.models.dto.ProductReviewDTO.class, com.example.models.dto.UserDTO.class,
        com.example.models.dto.UserNotificationDTO.class, com.example.models.dto.statistics.BuyStatsDTO.class, com.example.models.dto.statistics.FrequencyAddToCartStatsDTO.class,
        com.example.models.dto.statistics.VisitStatsDTO.class,

        com.example.models.kafka.model.RequestDto.class, com.example.models.kafka.model.KafkaMessage.class
})
@Import({com.example.models.config.EntityConfig.class})
public interface UserRepository extends JpaRepository<UserM, Long> {
    @Transactional
    UserM findFirstByName(String name);

    @Transactional
    UserM findFirstById(Long id);

    @Modifying
    @Query(value = "update users SET role=:role where id=:id", nativeQuery = true)
    @Transactional
    void updateRole(@Param("role") String role, @Param("id") Long id);

    UserM findByActivationCode(String code);

}
