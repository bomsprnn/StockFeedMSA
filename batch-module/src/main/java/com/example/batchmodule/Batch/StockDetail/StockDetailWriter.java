package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.StockDetail;
import com.example.batchmodule.Repository.StockDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
@Slf4j
@RequiredArgsConstructor
public class StockDetailWriter implements ItemWriter<List<StockDetail>> {
    private final StockDetailRepository stockDetailRepository;

    @Override
    public void write(Chunk<? extends List<StockDetail>> chunk) throws Exception {
        log.info("Writing items to database !!! ");

        List<? extends List<StockDetail>> items = chunk.getItems();
        List<StockDetail> list = new ArrayList<>();
        items.forEach(list::addAll);
        log.info("chunk size ={}",list.size());
        stockDetailRepository.saveAll(list);
    }
}
