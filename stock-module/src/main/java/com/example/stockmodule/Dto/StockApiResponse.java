package com.example.stockmodule.Dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StockApiResponse {
    private List<StockDto> stocks; // 주식 목록

}
