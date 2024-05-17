package com.example.stockmodule.Repository;

import com.example.stockmodule.Domain.Stock;
import com.example.stockmodule.Domain.StockMarket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySymbol(String symbol);
    Stock findByName(String name);

    List<Stock> findByMarket(StockMarket market);

    Page<Stock> findByNameContains(String name, Pageable pageable);
    Page<Stock> findBySymbolContains(String symbol, Pageable pageable);
    Page<Stock> findByMarket(StockMarket market, Pageable pageable);
}

