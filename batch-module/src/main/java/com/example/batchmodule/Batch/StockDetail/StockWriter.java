package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.Stock;
import com.example.batchmodule.Domain.StockDetail;
import com.example.batchmodule.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
@Slf4j
public class StockWriter implements ItemWriter<List<Stock>> {
    private final StockRepository stockRepository;

    @Override
    public void write(Chunk<? extends List<Stock>> chunk) throws Exception {
        chunk.getItems().forEach(stockRepository::saveAll);
    }
}
