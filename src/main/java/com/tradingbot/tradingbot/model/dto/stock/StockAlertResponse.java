package com.tradingbot.tradingbot.model.dto.stock;

import com.tradingbot.tradingbot.model.StockAlert;

public class StockAlertResponse {
    private Long id;
    private StockResponse stock;
    private String condition;
    private Double targetPrice;

    public StockAlertResponse(StockAlert stockAlert) {
        this.id = stockAlert.getId();
        this.stock = new StockResponse(stockAlert.getStock());
        this.condition = stockAlert.getCondition();
        this.targetPrice = stockAlert.getTargetPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StockResponse getStock() {
        return stock;
    }

    public void setStock(StockResponse stock) {
        this.stock = stock;
    }
    
    public Double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(Double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    
}
