package com.example.stockmodule.Controller;

import com.example.stockmodule.Service.StockParseService;
import com.example.stockmodule.Service.StocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class StockController {
    private final StockParseService stockParseService;
    private final StocksService stocksService;

    @GetMapping("/stocks")
    public void getStocks(@RequestParam String symbol){
       stockParseService.parseAndSaveStockData(symbol);
    }

    @GetMapping("/stock")
    public void getStock(){
        stocksService.fetchAndSaveStockData();
    }

}
