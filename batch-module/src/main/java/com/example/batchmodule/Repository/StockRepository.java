package com.example.batchmodule.Repository;

import com.example.batchmodule.Domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    public abstract Optional<Stock> findBySymbol(String symbol);

    @Modifying
    @Transactional
    @Query("DELETE FROM Stock")
    void deleteAllStocks();



}
