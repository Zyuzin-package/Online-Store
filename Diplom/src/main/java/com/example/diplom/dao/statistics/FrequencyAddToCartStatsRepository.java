package com.example.diplom.dao.statistics;

import com.example.diplom.domain.statistics.FrequencyAddToCartStats;
import com.example.diplom.domain.statistics.VisitStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface FrequencyAddToCartStatsRepository extends JpaRepository<FrequencyAddToCartStats,Long> {
    @Modifying
    @Query(value = "Select * from frequency_add_to_cart_stats where product_id=(Select products.id from products where title = :title)", nativeQuery = true)
    @Transactional
    List<FrequencyAddToCartStats> getAllBuyProductName(@Param("title") String title);

    @Modifying
    @Query(value = "Select distinct created from frequency_add_to_cart_stats", nativeQuery = true)
    @Transactional
    List<String> getUniqueDates();

    @Query(value = "SELECT COUNT(product_id) from frequency_add_to_cart_stats where frequency_add_to_cart_stats.created=:date and " +
            " frequency_add_to_cart_stats.product_id=:productId group by frequency_add_to_cart_stats.product_id, frequency_add_to_cart_stats.created", nativeQuery = true)
    Integer getFrequencyCountByDateAndProductId (@Param("date") LocalDateTime date , @Param("productId") Long productId);

}
