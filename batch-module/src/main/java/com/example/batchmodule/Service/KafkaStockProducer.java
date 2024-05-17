package com.example.batchmodule.Service;

import com.example.batchmodule.Domain.StockDetail;
import com.example.batchmodule.Dto.StockDetailDto;
import com.example.batchmodule.Dto.StockDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaStockProducer {
    private static final String TOPIC = "stock-update";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendStockUpdateMessage(List<StockDetail> stockDetails) {
        StockDetail stockDetail = stockDetails.get(0);
        StockDetailDto stockDetailDto = StockDetailDto.builder()
                .symbol(stockDetail.getSymbol())
                .open(stockDetail.getOpen())
                .high(stockDetail.getHigh())
                .low(stockDetail.getLow())
                .close(stockDetail.getClose())
                .volume(stockDetail.getVolume())
                .date(stockDetail.getDate())
                .build();
        log.info(String.format("Produce message: %s", stockDetailDto));
        kafkaTemplate.send(TOPIC, stockDetail.getSymbol());
    }

}
