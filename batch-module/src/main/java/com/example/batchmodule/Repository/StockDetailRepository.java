package com.example.batchmodule.Repository;

import com.example.batchmodule.Domain.StockDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {
    boolean existsBySymbolAndDate(String symbol, LocalDateTime date);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM stock_data.stock_detail WHERE date = (SELECT MAX(date) FROM stock_data.stock_detail)", nativeQuery = true)
    void deleteRecentStocks();


}
