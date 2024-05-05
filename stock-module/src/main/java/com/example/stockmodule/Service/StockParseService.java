package com.example.stockmodule.Service;

import com.example.stockmodule.Domain.Stock;
import com.example.stockmodule.Domain.StockDetail;
import com.example.stockmodule.Dto.Chartdata;
import com.example.stockmodule.Dto.Item;
import com.example.stockmodule.Dto.Protocol;
import com.example.stockmodule.Repository.StockDetailRepository;
import com.example.stockmodule.Repository.StockRepository;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
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

    @Autowired
    public StockParseService(WebClient.Builder webClientBuilder, StockDetailRepository stockDetailRepository, StockRepository stockRepository) {
        this.webClient = webClientBuilder.baseUrl("https://fchart.stock.naver.com").build();
        this.stockDetailRepository = stockDetailRepository;
        this.stockRepository = stockRepository;
    }
    public void parseAllStockData() {
        stockRepository.findAll().forEach(stock -> {
            parseAndSaveStockData(stock.getSymbol(), stock);
        });
    }

    public void parseAndSaveStockData(String symbol, Stock stock) {
        parseStockData(symbol).subscribe(xmlData -> {
            log.info("Stock data: {}", xmlData);
            List<StockDetail> stockDetails = convertXmlToStockDetails(xmlData, stock);

            log.info("Stock details: {}", stockDetails);

            stockDetailRepository.saveAll(stockDetails);
        });
    }
    public Mono<String> parseStockData(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/sise.nhn")
                        .queryParam("symbol", symbol)
                        .queryParam("timeframe", "day")
                        .queryParam("count", "600")
                        .queryParam("requestType", "0")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }

    private List<StockDetail> convertXmlToStockDetails(String xmlData, Stock stock) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            Protocol protocol = xmlMapper.readValue(xmlData, Protocol.class);
            Chartdata chartData = protocol.getChartdata();
            log.info("Chardata: {}", chartData);
            List<StockDetail> stockDetails = new ArrayList<>();
            for (Item item : chartData.getItems()) {

                log.info("item: {}", item);
                String[] parts = item.getData().split("\\|");
                log.info("parts: {}", parts);
                StockDetail stockDetail = StockDetail.builder()
                        .date(LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay())
                        .open(Long.valueOf(parts[1]))
                        .high(Long.valueOf(parts[2]))
                        .low(Long.valueOf(parts[3]))
                        .close(Long.valueOf(parts[4]))
                        .volume(Long.valueOf(parts[5]))
                        .stock(stock)
                        .build();
                log.info("Stock detail: {}", stockDetail);
                stockDetails.add(stockDetail);
            }
            return stockDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


}
