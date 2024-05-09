package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.Stock;
import com.example.batchmodule.Repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@RequiredArgsConstructor
@StepScope
@Slf4j
public class StockDetailReader implements ItemReader<Stock> {
    private final StockRepository stockRepository;

    private Iterator<Stock> stockIterator;

    @PostConstruct
    public void init() {
        List<Stock> stocks = stockRepository.findAll();
        this.stockIterator = stocks.iterator();
        log.info("Loaded {} stocks from database", stocks.size());
    }

    @Override
    public Stock read() throws Exception {
        if (stockIterator.hasNext()) {
            return stockIterator.next();
        }
        return null;
    }
}
