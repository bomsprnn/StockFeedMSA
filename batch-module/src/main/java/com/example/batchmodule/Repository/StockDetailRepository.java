package com.example.batchmodule.Repository;

import com.example.batchmodule.Domain.StockDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {
    boolean existsBySymbolAndDate(String symbol, LocalDateTime date);

}
