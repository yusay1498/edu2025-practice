package com.example.kafka.consumer.domain.repository;

import com.example.kafka.consumer.domain.entity.SalesItemSubtotal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesItemSubtotalRepository {
   Optional<SalesItemSubtotal> findByItemIdAndDate(int itemId, LocalDate date);
   List<SalesItemSubtotal> saveAll(List<SalesItemSubtotal> salesItemSubtotalList);
}
