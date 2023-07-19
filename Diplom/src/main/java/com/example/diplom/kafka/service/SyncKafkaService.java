package com.example.diplom.kafka.service;

import com.example.models.domain.statistics.VisitStats;
import com.example.models.dto.ProductDTO;
import com.example.models.dto.statistics.VisitStatsDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface SyncKafkaService {
    String get(List<VisitStatsDTO> visitStatsDTOS) throws TimeoutException;
}
