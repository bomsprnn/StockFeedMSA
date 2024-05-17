package com.example.batchmodule.Repository;

import com.example.batchmodule.Domain.StockDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional
public class StockBulkSaveRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveAll(List<StockDetail> stockDetail) {
        log.info("Batch size for save: {}", stockDetail.size());

        jdbcTemplate.batchUpdate("INSERT INTO stock_data.stock_detail (symbol, date, open, high, low, close, volume) VALUES (?, ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, stockDetail.get(i).getSymbol());
                        ps.setTimestamp(2, Timestamp.valueOf(stockDetail.get(i).getDate()));
                        ps.setLong(3, stockDetail.get(i).getOpen());
                        ps.setLong(4, stockDetail.get(i).getHigh());
                        ps.setLong(5, stockDetail.get(i).getLow());
                        ps.setLong(6, stockDetail.get(i).getClose());
                        ps.setLong(7, stockDetail.get(i).getVolume());
                        //log.info("StockDetail 뭐냐면: {}", stockDetail.get(i).getSymbol());
                    }

                    @Override
                    public int getBatchSize() {
                        return stockDetail.size();
                    }
                }
        );

    }

}
