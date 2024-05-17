package com.example.stockmodule.Repository;

import com.example.stockmodule.Domain.StockDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {
    @Query("SELECT sd FROM StockDetail sd WHERE sd.symbol = :symbol ORDER BY sd.date DESC")
    List<StockDetail> findTopNDaysBySymbolOrderByDateDesc(@Param("symbol") String symbol, Pageable pageable);

   // List<StockDetail> findTopNDaysBySymbolOrderByDateDesc(String symbol, int days);
}
