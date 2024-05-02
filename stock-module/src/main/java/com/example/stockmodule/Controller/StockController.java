package com.example.stockmodule.Controller;

import com.example.stockmodule.Service.StockParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockParseService stockParseService;

    @GetMapping("/stocks")
    public Mono<String> getStocks(@RequestParam String symbol){
        return stockParseService.parseStockData(symbol);
    }

}
