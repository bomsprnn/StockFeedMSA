package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.StockDetail;
import com.example.batchmodule.Repository.StockBulkSaveRepository;
import com.example.batchmodule.Repository.StockDetailRepository;
import com.example.batchmodule.Service.KafkaStockProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@StepScope
@Slf4j
@RequiredArgsConstructor
public class StockDetailWriter implements ItemWriter<List<StockDetail>> {
    private final JdbcTemplate jdbcTemplate;

    private final StockDetailRepository stockDetailRepository;
    private final KafkaStockProducer kafkaStockProducer;
    private final StockBulkSaveRepository stockBulkSaveRepository;

    @Override
    public void write(Chunk<? extends List<StockDetail>> chunk) throws Exception {
        //log.info("Writing items to database !!! ");

        List<? extends List<StockDetail>> items = chunk.getItems();
        List<StockDetail> list = new ArrayList<>();
        items.forEach(list::addAll);

//        List<StockDetail> filteredList = list.stream()
//                .filter(stockDetail -> !stockDetailRepository.existsBySymbolAndDate(stockDetail.getSymbol(), stockDetail.getDate()))
//                .collect(Collectors.toList()); // 중복되지 않는 항목만 필터링
////

        //log.info("chunk size ={}",list.size());

       stockBulkSaveRepository.saveAll(list);
       //log.info("stockDetailRepository.saveAll(list) 완료, list size = {}", list.size());

        //stockDetailRepository.saveAll(list);
       // kafkaStockProducer.sendStockUpdateMessage(filteredList);

    }

}
