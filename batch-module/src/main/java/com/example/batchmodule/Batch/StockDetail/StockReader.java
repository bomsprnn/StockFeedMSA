package com.example.batchmodule.Batch.StockDetail;

import com.example.batchmodule.Domain.Stock;
import com.example.batchmodule.Service.StocksService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
@RequiredArgsConstructor
public class StockReader implements ItemReader<List<Stock>> {

    private final StocksService stocksService;
    private boolean read = false;

    @Override
    public List<Stock> read() throws Exception {
        if (!read) {
            read = true;
            return stocksService.stockdatas();
        }
        return null;
    }
}
