package com.tradingbot.tradingbot.model.dto.stock;

import com.tradingbot.tradingbot.model.Stock;

public class StockResponse {
    private Long id;
    private String symbol;
    private String name;

    public StockResponse(Stock stock) {
        this.id = stock.getId();
        this.symbol = stock.getSymbol();
        this.name = stock.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
