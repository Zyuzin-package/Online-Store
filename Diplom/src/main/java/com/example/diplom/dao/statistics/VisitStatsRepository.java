package com.example.diplom.dao.statistics;

import com.example.diplom.domain.statistics.VisitStats;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitStatsRepository extends JpaRepository<VisitStats,Long> {
}
