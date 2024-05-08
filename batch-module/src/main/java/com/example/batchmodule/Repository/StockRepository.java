package com.example.batchmodule.Repository;

import com.example.batchmodule.Domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    public abstract Optional<Stock> findBySymbol(String symbol);

}
