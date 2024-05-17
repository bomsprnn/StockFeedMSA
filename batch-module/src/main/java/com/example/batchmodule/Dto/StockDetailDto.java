package com.example.batchmodule.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class StockDetailDto {
    private String symbol;
    private LocalDateTime date;
    private Long open;
    private Long high;
    private Long low;
    private Long close;
    private Long volume;

    @Builder
    public StockDetailDto(String symbol, LocalDateTime date, Long open, Long high, Long low, Long close, Long volume){
        this.symbol = symbol;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
}
