package com.example.batchmodule.Repository;

import com.example.batchmodule.Domain.StockDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDetailRepository extends JpaRepository<StockDetail, Long> {

}
