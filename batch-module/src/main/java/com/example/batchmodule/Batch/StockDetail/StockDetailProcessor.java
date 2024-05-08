package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.Stock;
import com.example.batchmodule.Domain.StockDetail;
import com.example.batchmodule.Service.StockParseService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@StepScope
public class StockDetailProcessor implements ItemProcessor<Stock, List<StockDetail>> {
    private final StockParseService stockParseService;
    @Override
    public List<StockDetail> process(Stock stock) throws Exception {
        List<StockDetail> stockDetails = stockParseService.getStockDetailList(stock.getSymbol(), stock)
                .collectList()
                .block();
        return stockDetails;
    }
}
