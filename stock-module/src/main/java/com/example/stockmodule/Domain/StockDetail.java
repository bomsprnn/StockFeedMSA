package com.example.stockmodule.Domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class StockDetail {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    private Stock stock;
    private LocalDateTime date;
    private Long open;
    private Long high;
    private Long low;
    private Long close;
    private Long volume;


}
