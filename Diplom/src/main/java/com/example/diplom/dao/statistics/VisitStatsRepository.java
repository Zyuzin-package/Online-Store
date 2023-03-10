package com.example.diplom.dao.statistics;

import com.example.diplom.domain.statistics.VisitStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitStatsRepository extends JpaRepository<VisitStats,Long> {
    @Modifying
    @Query(value = "Select * from visit_stats where product_id=(Select products.id from products where title = :title)", nativeQuery = true)
    @Transactional
    List<VisitStats> getAllBuyProductName(@Param("title") String title);
    @Modifying
    @Query(value = "Select * from visit_stats where created=:date", nativeQuery = true)
    @Transactional
    List<VisitStats> getAllByDate(@Param("date") LocalDateTime localDateTime);
    @Modifying
    @Query(value = "Select distinct created from visit_stats", nativeQuery = true)
    @Transactional
    List<String> getUniqueDates();

    @Query(value = "SELECT COUNT(product_id) from visit_stats where visit_stats.created=:date and " +
            " visit_stats.product_id=:productId group by visit_stats.product_id, visit_stats.created", nativeQuery = true)
    Integer getVisitCountByDateAndProductId (@Param("date")LocalDateTime date ,@Param("productId") Long productId);

}
