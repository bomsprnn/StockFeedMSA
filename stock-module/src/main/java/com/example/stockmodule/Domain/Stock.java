package com.example.stockmodule.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Stock {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String symbol; // 종목 코드
    private String name;
    @Enumerated(EnumType.STRING)
    private StockMarket market; // 코스피, 코스닥
    private Double rate; // 등락률
    private Long price; // 현재가



    @Builder
    public Stock(String symbol, String name, StockMarket market, Double rate, Long price){
        this.symbol = symbol;
        this.name = name;
        this.market = market;
        this.rate = rate;
        this.price = price;
    }



}
