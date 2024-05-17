package com.example.stockmodule.Service;

import com.example.stockmodule.Domain.Stock;
import com.example.stockmodule.Domain.StockMarket;
import com.example.stockmodule.Dto.StockDto;
import com.example.stockmodule.Repository.StockDetailRepository;
import com.example.stockmodule.Repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockDetailRepository stockDetailRepository;
    private final StockRepository stockRepository;


    // 이름으로 종목 검색
    public List<StockDto> searchStockByName(String name, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Stock> stocks = stockRepository.findByNameContains(name, pageable);
        return stocks.stream()
                .map(stock -> StockDto.builder()
                        .symbol(stock.getSymbol())
                        .stockName(stock.getName())
                        .market(stock.getMarket().toString())
                        .closePrice(stock.getPrice())
                        .fluctuationsRatio(stock.getRate())
                        .build())
                .toList();
    }

    // 종목 코드로 종목 검색
    public List<StockDto> searchStockBySymbol(String symbol, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Stock> stocks = stockRepository.findBySymbolContains(symbol, pageable);
        return stocks.stream()
                .map(stock -> StockDto.builder()
                        .symbol(stock.getSymbol())
                        .stockName(stock.getName())
                        .market(stock.getMarket().toString())
                        .closePrice(stock.getPrice())
                        .fluctuationsRatio(stock.getRate())
                        .build())
                .toList();
    }


    // 코스피, 코스닥 종목 검색
    public List<StockDto> searchStockByMarket(int market, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        StockMarket stockMarket = market==1 ? StockMarket.KOSPI : StockMarket.KOSDAQ;
        Page<Stock> stocks = stockRepository.findByMarket(stockMarket, pageable);
        return stocks.stream()
                .map(stock -> StockDto.builder()
                        .symbol(stock.getSymbol())
                        .stockName(stock.getName())
                        .market(stock.getMarket().toString())
                        .closePrice(stock.getPrice())
                        .fluctuationsRatio(stock.getRate())
                        .build())
                .toList();
    }

    // 이름 오름차순 정렬
    public Page<Stock> searchStocksSortedByName(int page, boolean ascending) { // ascending이 true면 오름차순, false면 내림차순
        Pageable pageable = PageRequest.of(page, 10, Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, "name"));
        return stockRepository.findAll(pageable);
    }

    // 어제 종가기준 정렬
    public List<StockDto> searchStocksSortedByPrice(int page, boolean isAscending) { // ascending이 true면 오름차순, false면 내림차순
        Sort.Direction sortDirection = isAscending ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sortDirection, "closePrice"));
        Page<Stock> stocks = stockRepository.findAll(pageable);
        return stocks.stream()
                .map(stock -> StockDto.builder()
                        .symbol(stock.getSymbol())
                        .stockName(stock.getName())
                        .market(stock.getMarket().toString())
                        .closePrice(stock.getPrice())
                        .fluctuationsRatio(stock.getRate())
                        .build())
                .toList();
    }

    // 한 종목 상세보기

    // 주가 차트 1개월
    // 주가 차트 1년
    // 주가 차트 3년
    // 주가 차트 5년
}
