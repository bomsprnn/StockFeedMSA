package com.example.stockmodule.Service;

import com.example.stockmodule.Domain.Stock;
import com.example.stockmodule.Domain.StockMarket;
import com.example.stockmodule.Dto.StockApiResponse;
import com.example.stockmodule.Dto.StockDto;
import com.example.stockmodule.Repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
public class StocksService {
    private final WebClient webClient;
    private final StockRepository stockRepository;

    private static final String KOSPIpath = "/api/stocks/marketValue/KOSPI";
    private static final String KOSDAQpath = "/api/stocks/marketValue/KOSDAQ";

    public StocksService(StockRepository stockRepository) {
        this.webClient = WebClient.builder().baseUrl("https://m.stock.naver.com").build();
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void fetchAndSaveStockData() {
        fetchAndSaveByMarket(KOSPIpath);
       fetchAndSaveByMarket(KOSDAQpath);
    }

    public void fetchAndSaveByMarket(String path) {
        Flux.range(1, Integer.MAX_VALUE)
                .delayElements(Duration.ofSeconds(1)) // 1초 딜레이 추가
                .flatMap(page -> fetchStockData(page, path  ))
                .takeUntil(stockList -> stockList.isEmpty())
                .flatMap(Flux::fromIterable)
                .map(this::convertToEntity)
                .collectList()
                .subscribe(this::saveUniqueStocks); // 변경된 부분
    }

    //@Transactional
    public void saveUniqueStocks(List<Stock> stocks) {
        stocks.forEach(stock -> {
            log.info("Method start: saveUniqueStocks by thread {}", Thread.currentThread().getName());
            boolean exists = stockRepository.findBySymbol(stock.getSymbol()).isPresent();
            if (!exists) {
                stockRepository.save(stock);
            }
            log.info("Method 종료: {} by thread {}", System.currentTimeMillis(), Thread.currentThread().getName());

        });
    }

    private Flux<List<StockDto>> fetchStockData(int page, String path) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("page", page)
                        .queryParam("pageSize", 100) // pageSize를 최대한 크게 설정하여 API 호출 수 줄이기
                        .build())
                .retrieve()
                .bodyToMono(StockApiResponse.class)
                .filter(response -> response.getStocks() != null)
                .map(StockApiResponse::getStocks)
                .flux();
    }


    private Stock convertToEntity(StockDto dto) {
        return Stock.builder()
                .symbol(dto.getItemCode())
                .name(dto.getStockName())
                .market(StockMarket.valueOf(dto.getSosok().equals("0") ? "KOSPI" : "KOSDAQ"))
                .rate(Double.parseDouble(String.valueOf(dto.getFluctuationsRatio())))
                .price(Long.parseLong(String.valueOf(dto.getClosePrice()).replaceAll(",", "")))
                .build();

    }
}
