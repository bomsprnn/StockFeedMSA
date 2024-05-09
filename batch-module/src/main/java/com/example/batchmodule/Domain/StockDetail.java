package com.example.batchmodule.Domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class StockDetail {

    @Id @GeneratedValue
    private Long id;
    private String symbol;
    private LocalDateTime date;
    private Long open;
    private Long high;
    private Long low;
    private Long close;
    private Long volume;

    @Builder
    public StockDetail(String symbol, LocalDateTime date, Long open, Long high, Long low, Long close, Long volume){
        this.symbol = symbol;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}
