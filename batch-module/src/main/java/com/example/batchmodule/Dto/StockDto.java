package com.example.batchmodule.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StockDto {
    private String itemCode; // 종목 코드
    private String stockName; // 주식 이름
    private String sosok; // 시장 구분 (0: 코스피, 1: 코스닥 등)
    private double fluctuationsRatio; // 등락률
    private long closePrice; // 현재가

    // 생성자, 게터 및 세터 생략

    // API 응답에서 받은 가격 문자열을 숫자로 변환
    public void setClosePrice(String closePrice) {
        this.closePrice = Long.parseLong(closePrice.replaceAll(",", ""));
    }
}
