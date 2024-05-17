package com.example.stockmodule.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class StockIndicator {

    @Id @GeneratedValue
    private Long id;
    private String symbol;
    private LocalDateTime date;
    private Double movingAverage12;
    private Double movingAverage20;
    private Double movingAverage26;
    private Double bollingerUpperBand;
    private Double bollingerLowerBand;
    private Double macd;

    @Builder
    public StockIndicator(String symbol, LocalDateTime date, Double movingAverage12, Double movingAverage20, Double movingAverage26, Double bollingerUpperBand, Double bollingerLowerBand, Double macd){
        this.symbol = symbol;
        this.date = date;
        this.movingAverage12 = movingAverage12;
        this.movingAverage20 = movingAverage20;
        this.movingAverage26 = movingAverage26;
        this.bollingerUpperBand = bollingerUpperBand;
        this.bollingerLowerBand = bollingerLowerBand;
        this.macd = macd;
    }
}
