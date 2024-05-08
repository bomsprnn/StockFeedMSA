package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.Stock;
import com.example.batchmodule.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@StepScope
@Slf4j
public class StockDetailReader implements ItemReader<Stock> {
    private final StockRepository stockRepository;

    @Override
    public Stock read() throws Exception {
        List<Stock> stocks = stockRepository.findAll();
        log.info("리스트 Stocks sizee: {}", stocks.size());
        return stocks.iterator().hasNext() ? stocks.iterator().next() : null;

    }
}
