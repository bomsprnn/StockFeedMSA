package com.example.stockmodule.Service;

import com.example.stockmodule.Domain.StockDetail;
import com.example.stockmodule.Domain.StockIndicator;
import com.example.stockmodule.Repository.StockDetailRepository;
import com.example.stockmodule.Repository.StockIndicatorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StockCalculateService {

    // 이동평균선 계산
    public Double calculateMovingAverage(List<StockDetail> stockDetails, int days) {
        return stockDetails.stream()
                .limit(days)
                .collect(Collectors.averagingDouble(StockDetail::getClose));
    }

    // 볼린저 밴드 계산
    public Double[] calculateBollingerBands(List<StockDetail> stockDetails, int days) {
        double movingAverage = calculateMovingAverage(stockDetails, days); // 이동평균선 계산
        double standardDeviation = calculateStandardDeviation(stockDetails, movingAverage, days); // 표준편차 계산

        double upperBand = movingAverage + (standardDeviation * 2);
        double lowerBand = movingAverage - (standardDeviation * 2);

        return new Double[]{upperBand, lowerBand};
    }

    // 표준편차 계산
    private double calculateStandardDeviation(List<StockDetail> stockDetails, double movingAverage, int days) {
        double sumOfSquares = stockDetails.stream()
                .limit(days)
                .mapToDouble(stockDetail -> Math.pow(stockDetail.getClose() - movingAverage, 2))
                .sum();
        return Math.sqrt(sumOfSquares / days);
    }

    // MACD 계산
    public Double calculateMacd(List<StockDetail> stockDetails) {
        double shortTermEma = calculateExponentialMovingAverage(stockDetails, 12);
        double longTermEma = calculateExponentialMovingAverage(stockDetails, 26);

        return shortTermEma - longTermEma;
    }

    // 지수이동평균(Exponential Moving Average, EMA) 계산
    private Double calculateExponentialMovingAverage(List<StockDetail> stockDetails, int days) {
        double smoothingConstant = 2.0 / (days + 1);
        double ema = stockDetails.get(0).getClose(); // 시작 지점의 종가를 초기 EMA로 사용

        for (int i = 1; i < days && i < stockDetails.size(); i++) {
            double closePrice = stockDetails.get(i).getClose();
            ema = (closePrice - ema) * smoothingConstant + ema;
        }

        return ema;
    }

    // 최종적으로 모든 지표를 계산하여 StockIndicator 객체 생성 및 반환
    public StockIndicator calculateIndicator(List<StockDetail> stockDetails) {
        String symbol = stockDetails.get(0).getSymbol();
        LocalDateTime date = stockDetails.get(0).getDate();
        Double ma12 = calculateMovingAverage(stockDetails, 12);
        Double ma20 = calculateMovingAverage(stockDetails, 20);
        Double ma26 = calculateMovingAverage(stockDetails, 26);
        Double[] bollingerBands = calculateBollingerBands(stockDetails, 20);
        Double macd = calculateMacd(stockDetails);
        return new StockIndicator(symbol, date, ma12, ma20, ma26, bollingerBands[0], bollingerBands[1], macd);
    }
}

