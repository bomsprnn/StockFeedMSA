package com.example.stockmodule.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StockParseService {
    private final WebClient webClient;
    @Autowired
    public StockParseService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://fchart.stock.naver.com").build();
    }
    public Mono<String> parseStockData(String symbol) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/sise.nhn")
                        .queryParam("symbol", symbol)
                        .queryParam("timeframe", "day")
                        .queryParam("count", "1250")
                        .queryParam("requestType", "0")
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
