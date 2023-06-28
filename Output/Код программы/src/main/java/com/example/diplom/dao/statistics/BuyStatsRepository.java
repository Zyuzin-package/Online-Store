package com.example.diplom.dao.statistics;

import com.example.diplom.domain.statistics.BuyStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface BuyStatsRepository extends JpaRepository<BuyStats, Long> {
    @Modifying
    @Query(value = "Select * from buy_stats where product_id=(Select products.id from products where title = :title)", nativeQuery = true)
    @Transactional
    List<BuyStats> getAllBuyProductName(@Param("title") String title);

    @Modifying
    @Query(value = "Select distinct created from buy_stats", nativeQuery = true)
    @Transactional
    List<String> getUniqueDates();

    @Query(value = "SELECT COUNT(product_id) from buy_stats where buy_stats.created=:date and " +
            " buy_stats.product_id=:productId group by buy_stats.product_id, buy_stats.created", nativeQuery = true)
    Integer getBuyCountByDateAndProductId (@Param("date") LocalDateTime date , @Param("productId") Long productId);

}
