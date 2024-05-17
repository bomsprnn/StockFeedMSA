package com.example.stockmodule.Repository;

import com.example.stockmodule.Domain.StockIndicator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockIndicatorRepository extends JpaRepository<StockIndicator, Long> {
    //StockIndicator findTopByOrderByDateDateDesc();
}
