package com.example.stockmodule.Repository;

import com.example.stockmodule.Domain.StockDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {

}
