package com.example.diplom.dao.statistics;

import com.example.diplom.domain.statistics.VisitStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VisitStatsRepository extends JpaRepository<VisitStats,Long> {
    @Modifying
    @Query(value = "Select * from visit_stats where product_id=(Select products.id from products where title = :title)", nativeQuery = true)
    @Transactional
    List<VisitStats> getAllBuyProductName(@Param("title") String title);
}
