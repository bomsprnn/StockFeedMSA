package com.example.batchmodule.Service;

import com.example.batchmodule.Domain.Stock;
import com.example.batchmodule.Domain.StockDetail;
import com.example.batchmodule.Dto.Chartdata;
import com.example.batchmodule.Dto.Item;
import com.example.batchmodule.Dto.Protocol;
import com.example.batchmodule.Repository.StockDetailRepository;
import com.example.batchmodule.Repository.StockRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StockParseService {
    private final WebClient webClient;
    private final StockDetailRepository stockDetailRepository;
    private final StockRepository stockRepository;
    private final RedisLockService redisLockService;


    @Autowired
    public StockParseService(WebClient.Builder webClientBuilder, StockDetailRepository stockDetailRepository, StockRepository stockRepository, RedisLockService redisLockService) {
        this.webClient = webClientBuilder.baseUrl("https://fchart.stock.naver.com").build();
        this.stockDetailRepository = stockDetailRepository;
        this.stockRepository = stockRepository;
        this.redisLockService = redisLockService;
    }

    public Flux<StockDetail> getStockDetailList(String symbol, String count) {
       //stockDetailRepository.deleteRecentStocks();

        String lockKey = "lock:stock:" + symbol;
        try {
            boolean lockAcquired = redisLockService.lock(lockKey);
            if (!lockAcquired) {
                log.info("락 획득 실패 !! 종목 코드: {}", symbol);
                return Flux.empty();
            }
            return parseStockData(symbol, count)
                    .flatMapMany(xmlData -> {
                        //log.info("Stock data: {}", xmlData);
                        return Flux.fromIterable(convertXmlToStockDetails(xmlData, symbol));
                    });
        } finally {
            redisLockService.unlock(lockKey);
        }
    }

    public Mono<String> parseStockData(String symbol, String count) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/sise.nhn")
                        .queryParam("symbol", symbol) // 종목 코드
                        .queryParam("timeframe", "day") // 일봉 데이터
                        .queryParam("count", count)  // 5년치 데이터 약 990개
                        .queryParam("requestType", "0")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    private List<StockDetail> convertXmlToStockDetails(String xmlData, String symbol) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            Protocol protocol = xmlMapper.readValue(xmlData, Protocol.class);
            Chartdata chartData = protocol.getChartdata();
           // log.info("Chardata: {}", chartData);
            List<StockDetail> stockDetails = new ArrayList<>();
            for (Item item : chartData.getItems()) {

                //log.info("item: {}", item);
                String[] parts = item.getData().split("\\|");
                //log.info("parts: {}", parts);
                StockDetail stockDetail = StockDetail.builder()
                        .date(LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay())
                        .open(Long.valueOf(parts[1]))
                        .high(Long.valueOf(parts[2]))
                        .low(Long.valueOf(parts[3]))
                        .close(Long.valueOf(parts[4]))
                        .volume(Long.valueOf(parts[5]))
                        .symbol(symbol)
                        .build();
                //log.info("Stock detail: {}", stockDetail);
                stockDetails.add(stockDetail);
            }
            return stockDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}