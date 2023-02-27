package com.example.diplom.dao.statistics;

import com.example.diplom.domain.statistics.BuyStats;
import com.example.diplom.domain.statistics.VisitStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BuyStatsRepository extends JpaRepository<BuyStats, Long> {
    @Modifying
    @Query(value = "Select * from buy_stats where product_id=(Select products.id from products where title = :title)", nativeQuery = true)
    @Transactional
    List<BuyStats> getAllBuyProductName(@Param("title") String title);
}
