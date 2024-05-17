package com.example.stockmodule.Service;

import com.example.stockmodule.Domain.StockDetail;
import com.example.stockmodule.Domain.StockIndicator;
import com.example.stockmodule.Dto.StockDetailDto;
import com.example.stockmodule.Repository.StockDetailRepository;
import com.example.stockmodule.Repository.StockIndicatorRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KakfaStockConsumerService {

    private final ObjectMapper objectMapper;
    private final StockCalculateService stockCalculateService;
    private final StockDetailRepository stockDetailRepository;
    private final StockIndicatorRepository stockIndicatorRepository;

    @KafkaListener(topics = "stock-update", groupId = "stock-group")
    public void stockDetailListen(String msg) throws Exception { //심볼 전달
        Pageable topN = PageRequest.of(0, 26);
        List<StockDetail> stockDetails = stockDetailRepository.findTopNDaysBySymbolOrderByDateDesc(msg, topN);
        //List<StockDetail> stockDetails = stockDetailRepository.findTopNDaysBySymbolOrderByDateDesc(msg, 26);
        StockIndicator stockIndicator = stockCalculateService.calculateIndicator(stockDetails);
        stockIndicatorRepository.save(stockIndicator);
    }
}